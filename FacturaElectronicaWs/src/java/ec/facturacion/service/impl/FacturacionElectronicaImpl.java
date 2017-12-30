/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturacion.service.impl;

import ec.facturacion.app.ComprobanteEnviado;
import ec.facturacion.autorizacion.AutorizacionComprobantesWs;
import ec.facturacion.exception.FacturaFirmaException;
import ec.facturacion.firmaxades.util.FirmarInterno;
import ec.facturacion.firmaxades.util.RecursosServices;
import ec.facturacion.firmaxades.util.xml.FileUtils;
import ec.facturacion.service.AutorizacionWs;
import ec.facturacion.service.EnvioWs;
import ec.facturacion.service.IFacturaElectronica;
import ec.facturacion.service.constantes.FirmaConstantes;
import ec.facturacion.ws.FirmaEnvio;
import ec.facturacionelectronica.aut.Autorizacion;
import ec.facturacionelectronica.aut.RespuestaComprobante;
import ec.facturacionelectronica.rec.Mensaje;
import ec.facturacionelectronica.rec.RespuestaSolicitud;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.constantes.ComprobanteConstantes;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import ec.facturaelectronica.service.FacturaService;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Christian
 */
@Stateless
public class FacturacionElectronicaImpl extends RecursosServices implements IFacturaElectronica {

    @EJB
    AdministracionService service;

    @EJB
    CertificadoTipoComprobanteService certificadoService;

    @EJB
    FacturaService facturaService;

    @EJB
    CertificadoService certificado;

    private int ambiente;
    private String pathArchivoPKCS12;
    private String clavePKCS12;
    private String pathDirectorioSalida;
    private String urlWsdlRecepcion;
    private String urlWsdlAutorizacion;

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FacturacionElectronicaImpl.class.getName());

    @Override
    public ComprobanteEnviado firmarEnviarAutorizar(byte[] archivoAFirmar, String email, String codigoEmpresa) throws Exception {
        ComprobanteEnviado respuesta = null;

        try {
            String tmp = recurso.getProperty("path.certificados.tmp").concat(certificado.obtenerArchivoTemporal()).trim();
            FileUtils.byteToFile(archivoAFirmar, tmp);
            File archivoXML = new File(tmp);

            if (archivoXML.exists() == true) {

                respuesta = firmarEnviarAutorizarComprobante(archivoXML, email, codigoEmpresa);

            }

        } catch (FacturaFirmaException ex) {
            Logger.getLogger(FacturacionElectronicaImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {

        }
        return respuesta;
    }

    @Override
    public Autorizacion consultaAutorizacion(String claveDeAcceso)  throws FacturaFirmaException{

        Autorizacion respuesta;

        obtenerParametrosSistema(ambiente);
        respuesta = consultaAutorizacionSRI(claveDeAcceso);

        return respuesta;
    }

    private void obtenerParametrosSistema(int ambiente) {

        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream("/resources/config.properties"));

            if (ambiente == 1) {
                this.setUrlWsdlRecepcion(prop.getProperty("urlWsdlRecepcion"));
                this.setUrlWsdlAutorizacion(prop.getProperty("urlWsdlAutorizacion"));
            } else if (ambiente == 2) {
                this.setUrlWsdlRecepcion(prop.getProperty("urlWsdlRecepcionProduccion"));
                this.setUrlWsdlAutorizacion(prop.getProperty("urlWsdlAutorizacionProduccion"));
            }
        } catch (Exception ex) {
            getLog().error(ex);
        }

    }

    /**
     * Funcion que realiza en solo paso la firma, envio y consulta de
     * autorizacion a los WS del SRI
     *
     * @param archivoXML
     * @return un objeto tipo ComprobanteEnviado con los datos del comprobante
     * autorizado
     * @throws ec.facturacion.exception.FacturaFirmaException
     */
    private ComprobanteEnviado firmarEnviarAutorizarComprobante(File archivoXML, String emailNotificacion, String codigoEmpresa) throws FacturaFirmaException {

        ComprobanteEnviado comprobante = new ComprobanteEnviado();
        TipoComprobante tipoComprobante;
        Comprobante comprobanteFirma;

        String claveAcceso = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/claveAcceso");
        Integer amb = Integer.valueOf(FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/ambiente"));
        String ruc = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/ruc");
        String codDoc = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/codDoc");
        String nombreArchivoFirmado = recurso.getProperty("path.certificados.firmados") + claveAcceso + ".xml";
        String nombreArchivoFirma = claveAcceso + ".xml";
        String identificacion = null;
        String secuencial = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/secuencial");
        String totalStr;
        //String numAutorizacion;
        BigDecimal total = BigDecimal.ZERO;
        String razonSocial = null;

        obtenerParametrosSistema(amb);

        //verificacion del ruc si existe en el sistema
        Empresa valEmpresa;
        try {

            valEmpresa = service.buscaEmpresaActiva(ruc, codigoEmpresa);

            //valida empresa
            if (valEmpresa == null) {
                comprobante.setNovedades("Empresa no registrada en el sistema");

                return comprobante;
            }
            //verifica numero de comprobante
            tipoComprobante = service.buscarTipoComprobante(codDoc);

            if (tipoComprobante == null) {
                comprobante.setNovedades("El tipo de comprobante no se encuentra registrado en el sistema");

                return comprobante;
            }

            if (tipoComprobante.getCodigoTipo().equals(ComprobanteConstantes.FACTURA)) {
                identificacion = FileUtils.obtenerValorXML(archivoXML, "/*/infoFactura/identificacionComprador");
                totalStr = FileUtils.obtenerValorXML(archivoXML, "/*/infoFactura/totalSinImpuestos");
                total = new BigDecimal(totalStr);
                razonSocial = FileUtils.obtenerValorXML(archivoXML, "/*/infoFactura/razonSocialComprador");

            }

            //buscar certificado para la emresa y el certificado
            CertificadoTipoComprobante certificado = certificadoService.buscarCertificado(valEmpresa, tipoComprobante);

            if (certificado == null) {
                comprobante.setNovedades("No se encuentra el certificado para el comprobante");

                return comprobante;
            }

            //guardar comprobante a firmar
            //firmar archivo
            File archivoFirmado;
            try {
                archivoFirmado = firmarArchivo(archivoXML, nombreArchivoFirmado, certificado);

                comprobanteFirma = new Comprobante();
                comprobanteFirma.setAmbienteComprobante(amb == 2 ? "PRODUCCION" : "PRUEBAS");
                comprobanteFirma.setArchivoComprobante(nombreArchivoFirma);
                comprobanteFirma.setClaveComprobante(claveAcceso);

                comprobanteFirma.setFechaComprobante(new Date());
                comprobanteFirma.setFechaSistemaComprobante(new Date());
                comprobanteFirma.setIdEmpresa(valEmpresa);
                comprobanteFirma.setIdTipoComprobante(tipoComprobante);
                comprobanteFirma.setIdentificadorComprobante(identificacion);

                comprobanteFirma.setNumeroAutorizacionComprobante(null);
                comprobanteFirma.setSecuencialComprobante(secuencial);
                comprobanteFirma.setTotalComprobante(total);
                comprobanteFirma.setTipoMensajeComprobante(null);
                comprobanteFirma.setEmail(emailNotificacion);
                comprobanteFirma.setNotificaEmail(Boolean.FALSE);

            } catch (Exception ex) {
                throw new FacturaFirmaException(ex.getMessage());
            }
            log.debug("Tamaño archivo firmado: " + archivoFirmado.length());

            RespuestaSolicitud respuesta = EnvioWs.obtenerRespuestaEnvio(archivoFirmado, this.urlWsdlRecepcion);

            if (respuesta.getEstado().equals(FirmaConstantes.DEVUELTA)) {
                comprobante.setNovedades(armarNovedades(respuesta));
                comprobante.setEstadoComprobante(respuesta.getEstado());

                return comprobante;
            }

            log.debug("Novedades de WS Recepcion: " + respuesta);
            if (respuesta.getEstado().equals(FirmaConstantes.RECIBIDA)) {

                comprobanteFirma.setEstadoComprobante(respuesta.getEstado());
                comprobanteFirma.setFechaAutorizacionComprobante(new Date());
                facturaService.guardarComprobanteWS(comprobanteFirma);
                comprobante.setEstadoComprobante(comprobanteFirma.getEstadoComprobante());

            } else {
                comprobante.setNovedades(armarNovedades(respuesta));
                comprobante.setEstadoComprobante(respuesta.getEstado());
            }

        } catch (ServicesException ex) {
            comprobante.setNovedades(ex.getMessage());
        }

        return comprobante;
    }

    public void notificaEmail(Comprobante comprobante) {
        try {
            //TODO write your implementation code here:
            InitialContext context = new InitialContext();
            QueueConnectionFactory cF = (QueueConnectionFactory) context.lookup("jms/queueFactoryFactura");
            Queue queue = (Queue) context.lookup("jms/QueueFactura");

            Connection cnn = cF.createConnection();
            Session sess = cnn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messProd = sess.createProducer(queue);

            messProd.send(sess.createObjectMessage(comprobante));

            messProd.close();
            sess.close();
            cnn.close();
            System.err.println("El mensaje se envio correctamente");

        } catch (NamingException ex) {
            Logger.getLogger(FirmaEnvio.class.getName()).log(Level.SEVERE, null, ex);

        } catch (JMSException ex) {
            Logger.getLogger(FirmaEnvio.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private String armarNovedades(RespuestaSolicitud respuesta) {
        StringBuilder novedadesFirma = new StringBuilder();
        for (ec.facturacionelectronica.rec.Comprobante comp : respuesta.getComprobantes().getComprobante()) {
            for (Mensaje men : comp.getMensajes().getMensaje()) {
                novedadesFirma.append(men.getMensaje()).append("-").append(men.getTipo());
            }
        }

        return novedadesFirma.toString();
    }

    /**
     * Funcion que realiza la consulta del estado de autorizacion a los WS del
     * SRI de un comprobante previamente enviado
     *
     * @param claveDeAcceso
     * @return un objeto tipo Autorizacion con los datos del comprobante
     * autorizado o no autorizado
     */
    private Autorizacion consultaAutorizacionSRI(String claveDeAcceso) throws FacturaFirmaException {

        // consulto si ya fue autorizado, devuelve el FILE ya con la autorizacion
        RespuestaComprobante respuesta = null;
        Autorizacion autorizacion = null;

        respuesta = new AutorizacionWs(this.urlWsdlAutorizacion).llamadaWSAutorizacionInd(claveDeAcceso);

        if (null == respuesta) {
            throw new FacturaFirmaException("Error en el procedimiento de consulta");
        } else {
            for (Autorizacion item : respuesta.getAutorizaciones().getAutorizacion()) {
                autorizacion = item;
            }
        }

        if (null == autorizacion) {
            throw new FacturaFirmaException("Error en el procedimiento de autorizacion");
        }
        return autorizacion;
    }

    /**
     * Firma un documento XML bien formado
     *
     * @param archivoXML
     * @param nombreArchivoFirmado
     * @return un objeto File con el archivo firmado digitalmente
     *
     */
    private File firmarArchivo(File archivoXML, String nombreArchivoFirmado, CertificadoTipoComprobante certificado) throws Exception {

        String pathArchivoTemporalDeployado = null;
        pathArchivoTemporalDeployado = archivoXML.getParent();

        // firmo el documento
        pathArchivoPKCS12 = recurso.getProperty("path.certificados") + certificado.getCertificado().getNombreArchivoCertificado();
        System.err.println(pathArchivoPKCS12);
        clavePKCS12 = service.devolverClavePk12(certificado.getCertificado().getClaveCertificado());
        FirmarInterno firmador = new FirmarInterno(pathArchivoPKCS12,
                clavePKCS12, pathArchivoTemporalDeployado, archivoXML.getPath(),
                nombreArchivoFirmado);

        firmador.execute();
        //File archivoFirmado = new File(pathArchivoTemporalDeployado + "/" + nombreArchivoFirmado);
        File archivoFirmado = new File(nombreArchivoFirmado);

        return archivoFirmado;
    }

    @Override
    public boolean validarP12(String filenameP12, String clave) {
        FirmarInterno firma = new FirmarInterno(filenameP12, clave);

        return firma.validar();

    }

    @Override
    public ComprobanteEnviado generarAutorizacion(String claveDeAcceso, int ambiente, int numeroIntentos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the ambiente
     */
    /**
     * @return the ambiente
     */
    public int getAmbiente() {
        return ambiente;
    }

    /**
     * @param ambiente the ambiente to set
     */
    public void setAmbiente(int ambiente) {
        this.ambiente = ambiente;
    }

    /**
     * @return the pathArchivoPKCS12
     */
    public String getPathArchivoPKCS12() {
        return pathArchivoPKCS12;
    }

    /**
     * @param pathArchivoPKCS12 the pathArchivoPKCS12 to set
     */
    public void setPathArchivoPKCS12(String pathArchivoPKCS12) {
        this.pathArchivoPKCS12 = pathArchivoPKCS12;
    }

    /**
     * @return the clavePKCS12
     */
    public String getClavePKCS12() {
        return clavePKCS12;
    }

    /**
     * @param clavePKCS12 the clavePKCS12 to set
     */
    public void setClavePKCS12(String clavePKCS12) {
        this.clavePKCS12 = clavePKCS12;
    }

    /**
     * @return the pathDirectorioSalida
     */
    public String getPathDirectorioSalida() {
        return pathDirectorioSalida;
    }

    /**
     * @param pathDirectorioSalida the pathDirectorioSalida to set
     */
    public void setPathDirectorioSalida(String pathDirectorioSalida) {
        this.pathDirectorioSalida = pathDirectorioSalida;
    }

    /**
     * @return the urlWsdlRecepcion
     */
    public String getUrlWsdlRecepcion() {
        return urlWsdlRecepcion;
    }

    /**
     * @param urlWsdlRecepcion the urlWsdlRecepcion to set
     */
    public void setUrlWsdlRecepcion(String urlWsdlRecepcion) {
        this.urlWsdlRecepcion = urlWsdlRecepcion;
    }

    /**
     * @return the urlWsdlAutorizacion
     */
    public String getUrlWsdlAutorizacion() {
        return urlWsdlAutorizacion;
    }

    /**
     * @param urlWsdlAutorizacion the urlWsdlAutorizacion to set
     */
    public void setUrlWsdlAutorizacion(String urlWsdlAutorizacion) {
        this.urlWsdlAutorizacion = urlWsdlAutorizacion;
    }

    /**
     * @return the log
     */
    public static org.apache.log4j.Logger getLog() {
        return log;
    }

    /**
     * @param aLog the log to set
     */
    public static void setLog(org.apache.log4j.Logger aLog) {
        log = aLog;
    }

}

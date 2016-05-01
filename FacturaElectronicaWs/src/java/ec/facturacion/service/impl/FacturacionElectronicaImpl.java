/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturacion.service.impl;

import ec.facturacion.app.ComprobanteEnviado;
import ec.facturacion.autorizacion.AutorizacionComprobantesWs;
import ec.facturacion.autorizacion.EnvioComprobantesWs;
import ec.facturacion.exception.FacturaFirmaException;
import ec.facturacion.firmaxades.util.FirmaPKCS12;
import ec.facturacion.firmaxades.util.FirmarInterno;
import ec.facturacion.firmaxades.util.xml.FileUtils;
import ec.facturacion.service.IFacturaElectronica;
import ec.facturacion.service.constantes.FirmaConstantes;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Cliente;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.constantes.ComprobanteConstantes;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import ec.facturaelectronica.service.FacturaService;
import ec.gob.sri.comprobantes.ws.MensajeXml;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Christian
 */
@Stateless
public class FacturacionElectronicaImpl implements IFacturaElectronica {

    @EJB
    AdministracionService service;

    @EJB
    CertificadoTipoComprobanteService certificadoService;

    @EJB
    FacturaService facturaService;

    private int ambiente;
    private String pathArchivoPKCS12;
    private String clavePKCS12;
    private String pathDirectorioSalida;
    private String urlWsdlRecepcion;
    private String urlWsdlAutorizacion;
    private ResourceBundle recursos = ResourceBundle.getBundle("resources.config");

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FacturacionElectronicaImpl.class.getName());

    @Override
    public ComprobanteEnviado firmarEnviarAutorizar(byte[] archivoAFirmar) throws Exception {
        ComprobanteEnviado respuesta = null;

        try {

            FileUtils.byteToFile(archivoAFirmar, "temp.xml");
            File archivoXML = new File("temp.xml");

            if (archivoXML.exists() == true) {

                respuesta = firmarEnviarAutorizarComprobante(archivoXML);

            }

        } catch (FacturaFirmaException ex) {
            Logger.getLogger(FacturacionElectronicaImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {

        }
        return respuesta;
    }

    @Override
    public Autorizacion consultaAutorizacion(String claveDeAcceso, int ambiente, int numeroIntentos) {

        Autorizacion respuesta;

        obtenerParametrosSistema(ambiente);
        respuesta = consultaAutorizacion(claveDeAcceso, numeroIntentos);

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
    private ComprobanteEnviado firmarEnviarAutorizarComprobante(File archivoXML) throws FacturaFirmaException {

        ComprobanteEnviado comprobante = new ComprobanteEnviado();
        TipoComprobante tipoComprobante;
        Comprobante comprobanteFirma;

        String claveAcceso = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/claveAcceso");
        Integer amb = Integer.valueOf(FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/ambiente"));
        String ruc = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/ruc");
        String codDoc = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/codDoc");
        String nombreArchivoFirmado = recursos.getString("path.certificados") + claveAcceso + ".xml";
        String nombreArchivoFirma = claveAcceso + ".xml";
        String identificacion = null;
        String secuencial = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/secuencial");
        String totalStr;
        //String numAutorizacion;
        BigDecimal total = BigDecimal.ZERO;
        String razonSocial = null;
        String email = null;
        StringBuilder novedadesFirma = new StringBuilder();

        obtenerParametrosSistema(amb);

        //verificacion del ruc si existe en el sistema
        Empresa valEmpresa;
        try {

            valEmpresa = service.buscaEmpresaActiva(ruc);

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
                email = FileUtils.obtenerValorXML(archivoXML, "/*/infoAdicional/campoAdicional[@nombre='Email'] ");
                
            }

            //buscar certificado para la emresa y el certificado
            CertificadoTipoComprobante certificado = certificadoService.buscarCertificado(valEmpresa, tipoComprobante);

            if (certificado == null) {
                comprobante.setNovedades("No se encuentra el certificado para el comprobante");

                return comprobante;
            }

            //guardar comprobante a firmar
            comprobanteFirma = new Comprobante();
            comprobanteFirma.setAmbienteComprobante(this.ambiente == 1 ? "PRODUCCION" : "PRUEBAS");
            comprobanteFirma.setArchivoComprobante(nombreArchivoFirma);
            comprobanteFirma.setClaveComprobante(claveAcceso);
            comprobanteFirma.setEstadoComprobante("ENVIADO");
            comprobanteFirma.setFechaAutorizacionComprobante(null);
            comprobanteFirma.setFechaComprobante(new Date());
            comprobanteFirma.setFechaSistemaComprobante(new Date());
            comprobanteFirma.setIdEmpresa(valEmpresa);
            comprobanteFirma.setIdTipoComprobante(tipoComprobante);
            comprobanteFirma.setIdentificadorComprobante(identificacion);
            comprobanteFirma.setMensajeComprobante("ENVIO...");
            comprobanteFirma.setNumeroAutorizacionComprobante(null);
            comprobanteFirma.setSecuencialComprobante(secuencial);
            comprobanteFirma.setTotalComprobante(total);
            comprobanteFirma.setTipoMensajeComprobante(null);

            facturaService.guardarComprobanteWS(comprobanteFirma);
            //firmar archivo
            File archivoFirmado;
            try {
                archivoFirmado = firmarArchivo(archivoXML, nombreArchivoFirmado, certificado);
            } catch (Exception ex) {
                throw new FacturaFirmaException(ex.getMessage());
            }
            log.debug("Tamaño archivo firmado: " + archivoFirmado.length());

            // envio para comprobante al SRI
            RespuestaSolicitud respuesta = EnvioComprobantesWs.obtenerRespuestaEnvio(archivoFirmado, this.urlWsdlRecepcion);

            //guardar estado recibido
            comprobanteFirma.setEstadoComprobante(respuesta.getEstado());
            facturaService.actualizarComprobanteWS(comprobanteFirma);

            if (respuesta.getEstado().equals(FirmaConstantes.DEVUELTA)) {

                comprobante.setNovedades(respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje());

                for (ec.gob.sri.comprobantes.ws.Comprobante comp : respuesta.getComprobantes().getComprobante()) {
                    for (MensajeXml men : comp.getMensajes().getMensaje()) {
                        novedadesFirma.append(men.getMensaje()).append("-").append(men.getTipo());
                    }
                }
                comprobante.setNovedades(novedadesFirma.toString());

                return comprobante;
            }

            log.debug("Novedades de WS Recepcion: " + respuesta);

            // consulto si ya fue autorizado, devuelve el FILE ya con la autorizacion
            AutorizacionComprobantesWs.RespuestaAutorizacion respuestaAuto = AutorizacionComprobantesWs.autorizarComprobanteIndividual(claveAcceso,
                    archivoFirmado.getName(), this.urlWsdlAutorizacion);
            log.debug("Novedades de WS Autorizacion: " + respuestaAuto.novedades);

            // numAutorizacion = FileUtils.obtenerValorXML(archivoFirmado, "/*/numeroAutorizacion");
            if ("".equals(respuestaAuto.respuesta.getAutorizaciones().getAutorizacion().get(0).getNumeroAutorizacion())) {
                comprobanteFirma.setEstadoComprobante("NO AUTORIZADO");
            } else {
                comprobanteFirma.setEstadoComprobante(respuestaAuto.respuesta.getAutorizaciones().getAutorizacion().get(0).getEstado());
                comprobanteFirma.setNumeroAutorizacionComprobante(respuestaAuto.respuesta.getAutorizaciones().getAutorizacion().get(0).getNumeroAutorizacion());
                comprobanteFirma.setFechaAutorizacionComprobante(new Date());
                //generar cliente
                if (comprobanteFirma.getEstadoComprobante().equals("AUTORIZADO")) {
                    if (!service.verificarCliente(ruc)) {
                        Cliente cliente = new Cliente();
                        cliente.setEmailCliente(email);
                        cliente.setEstadoCliente("R");
                        cliente.setIdEmpresa(valEmpresa);
                        cliente.setNombresCliente(razonSocial);
                        cliente.setRucCliente(ruc);
                    }
                }

            }
            System.err.println(respuestaAuto.novedades);

            comprobanteFirma.setMensajeComprobante(respuestaAuto.novedades);

            facturaService.actualizarComprobanteWS(comprobanteFirma);

            log.debug("Estado comprobante:" + comprobante.getEstadoComprobante());

        } catch (ServicesException ex) {
            comprobante.setNovedades(ex.getMessage());
        }

        return comprobante;
    }

    /**
     * Funcion que realiza la consulta del estado de autorizacion a los WS del
     * SRI de un comprobante previamente enviado
     *
     * @param claveDeAcceso
     * @param numeroIntentos
     * @return un objeto tipo Autorizacion con los datos del comprobante
     * autorizado o no autorizado
     */
    private Autorizacion consultaAutorizacion(String claveDeAcceso, int numeroIntentos) {

        // consulto si ya fue autorizado, devuelve el FILE ya con la autorizacion
        RespuestaComprobante respuesta = null;
        Autorizacion autorizacion = null;

        // llama al WS de autorizacion y hace N intentos hasta que llegue lleno el objeto RespuestaComprobante
        for (int i = 0; i < numeroIntentos; i++) {
            respuesta = new AutorizacionComprobantesWs(this.urlWsdlAutorizacion).llamadaWSAutorizacionInd(claveDeAcceso);

            if (respuesta.getAutorizaciones().getAutorizacion().isEmpty() == false) {
                break;
            }
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException ex) {
                log.error("Excepcion debida a función: Thread.currentThread().sleep(500)/n  ", ex);
            }
        }

        if (respuesta != null) {
            for (Autorizacion item : respuesta.getAutorizaciones().getAutorizacion()) {
                autorizacion = item;
            }
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
        pathArchivoPKCS12 = recursos.getString("path.certificados") + certificado.getCertificado().getNombreArchivoCertificado();
        System.err.println(pathArchivoPKCS12);
        clavePKCS12 = service.devolverClavePk12(certificado.getCertificado().getClaveCertificado());
        FirmarInterno firmador = new FirmarInterno(pathArchivoPKCS12,
                clavePKCS12, pathArchivoTemporalDeployado, archivoXML.getPath(),
                nombreArchivoFirmado);
//aqui me quede

        firmador.execute();
        //File archivoFirmado = new File(pathArchivoTemporalDeployado + "/" + nombreArchivoFirmado);
        File archivoFirmado = new File(nombreArchivoFirmado);

        return archivoFirmado;
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

    /**
     * @return the recursos
     */
    public ResourceBundle getRecursos() {
        return recursos;
    }

    /**
     * @param recursos the recursos to set
     */
    public void setRecursos(ResourceBundle recursos) {
        this.recursos = recursos;
    }

    @Override
    public boolean validarP12(String filenameP12, String clave) {
        FirmarInterno firma = new FirmarInterno(filenameP12, clave);

        return firma.validar();

    }

}

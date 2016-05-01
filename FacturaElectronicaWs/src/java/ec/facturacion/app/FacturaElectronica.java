/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.app;

import ec.facturacion.autorizacion.AutorizacionComprobantesWs;
import ec.facturacion.autorizacion.EnvioComprobantesWs;
import ec.facturacion.exception.FacturaFirmaException;
import ec.facturacion.firmaxades.util.FirmarInterno;
import ec.facturacion.firmaxades.util.xml.FileUtils;
import ec.facturaelectronica.service.AdministracionService;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.io.File;
import java.util.logging.Level;
import javax.ejb.EJB;


import org.apache.log4j.Logger;

/**
 *
 * @author Gabriel Eguiguren Peñarreta
 */

public class FacturaElectronica  {

    @EJB
    AdministracionService admService;
   
    
    static Logger log = Logger.getLogger(FacturaElectronica.class.getName());

    public FacturaElectronica() {
    }

    
    public FacturaElectronica(int ambiente) {
        //obtenerParametrosSistema(ambiente);
    }

    String pathArchivoPKCS12;
    String clavePKCS12;
    String pathDirectorioSalida;
    String urlWsdlRecepcion;
    String urlWsdlAutorizacion;

    /**
     * Funcion que realiza en solo paso la firma, envio y consulta de
     * autorizacion a los WS del SRI
     *
     * @param archivoXML
     * @return un objeto tipo ComprobanteEnviado con los datos del comprobante
     * autorizado
     * @throws ec.facturacion.exception.FacturaFirmaException
     */
    public ComprobanteEnviado firmarEnviarAutorizarComprobante(File archivoXML) throws FacturaFirmaException {

        ComprobanteEnviado comprobante = new ComprobanteEnviado();
        String claveAcceso = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/claveAcceso");
        String ruc = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/ruc");
        String nombreArchivoFirmado = claveAcceso + ".xml";

        

        //verificacion del ruc si existe en el sistema
        boolean valEmpresa;
        try {
           

          
            //firmar archivo
            File archivoFirmado = firmarArchivo(archivoXML, nombreArchivoFirmado);
            log.debug("Tamaño archivo firmado: " + archivoFirmado.length());

            // envio para comprobante al SRI
            RespuestaSolicitud respuesta = EnvioComprobantesWs.obtenerRespuestaEnvio(archivoFirmado, this.urlWsdlRecepcion);
            log.debug("Novedades de WS Recepcion: " + respuesta);

            // consulto si ya fue autorizado, devuelve el FILE ya con la autorizacion
//            String novedades = AutorizacionComprobantesWs.autorizarComprobanteIndividual(claveAcceso,
//                    archivoFirmado.getName(), this.urlWsdlAutorizacion);
//            log.debug("Novedades de WS Autorizacion: " + novedades);
//
//            // lleno el objeto con sus respectivos valores
//            comprobante.archivo = archivoFirmado;
//            comprobante.estadoComprobante = FileUtils.obtenerValorXML(archivoFirmado, "/*/estado");
//            comprobante.numAutorizacion = FileUtils.obtenerValorXML(archivoFirmado, "/*/numeroAutorizacion");
//            comprobante.novedades = novedades;
//            log.debug("Estado comprobante:" + comprobante.estadoComprobante);

        } catch (Exception ex) {

        }

        return comprobante;
    }

    /**
     * Funcion que realiza en solo paso la firma digital de un comprobante,
     * envio del mismos Web Service de recepción del SRI
     *
     * @param archivoXML
     * @return un objeto tipo ComprobanteEnviado con los datos del comprobante
     * autorizado
     * @throws java.lang.Exception
     */
    public RespuestaSolicitud firmarEnviarComprobante(File archivoXML) throws Exception {

        String claveAcceso = FileUtils.obtenerValorXML(archivoXML, "/*/infoTributaria/claveAcceso");
        String nombreArchivoFirmado = claveAcceso + ".xml";

        //firmar archivo
        File archivoFirmado = firmarArchivo(archivoXML, nombreArchivoFirmado);
        log.debug("Tamaño archivo firmado: " + archivoFirmado.length());

        // envio para comprobante al SRI
        RespuestaSolicitud respuesta = EnvioComprobantesWs.obtenerRespuestaEnvio(archivoFirmado, this.urlWsdlRecepcion);
        log.debug("Novedades de WS Recepcion: " + respuesta);

        return respuesta;
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
    public Autorizacion consultaAutorizacion(String claveDeAcceso, int numeroIntentos) {

        // consulto si ya fue autorizado, devuelve el FILE ya con la autorizacion
        RespuestaComprobante respuesta = null;
        Autorizacion autorizacion = null;
        try {
           // boolean  valEmpresa = admService.buscaEmpresaActiva("aa");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FacturaElectronica.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * @throws java.lang.Exception
     *
     */
    public File firmarArchivo(File archivoXML, String nombreArchivoFirmado) throws Exception {

        String pathArchivoTemporalDeployado = null;
        pathArchivoTemporalDeployado = archivoXML.getParent();

        // firmo el documento
        FirmarInterno firmador = new FirmarInterno(pathArchivoPKCS12,
                clavePKCS12, pathArchivoTemporalDeployado, archivoXML.getPath(),
                nombreArchivoFirmado);

        firmador.execute();
        //File archivoFirmado = new File(pathArchivoTemporalDeployado + "/" + nombreArchivoFirmado);
        File archivoFirmado = new File(nombreArchivoFirmado);

        return archivoFirmado;
    }

    public String getPathArchivoPKCS12() {
        return pathArchivoPKCS12;
    }

    public void setPathArchivoPKCS12(String pathArchivoPKCS12) {
        this.pathArchivoPKCS12 = pathArchivoPKCS12;
    }

    public String getClavePKCS12() {
        return clavePKCS12;
    }

    public void setClavePKCS12(String clavePKCS12) {
        this.clavePKCS12 = clavePKCS12;
    }

    public String getPathDirectorioSalida() {
        return pathDirectorioSalida;
    }

    public void setPathDirectorioSalida(String pathDirectorioSalida) {
        this.pathDirectorioSalida = pathDirectorioSalida;
    }

  

}

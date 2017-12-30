package ec.facturacion.service;

import ec.facturacion.firmaxades.util.xml.FileUtils;
import ec.facturacionelectronica.rec.Comprobante;
import ec.facturacionelectronica.rec.Mensaje;
import ec.facturacionelectronica.rec.RecepcionComprobantesOffline;
import ec.facturacionelectronica.rec.RecepcionComprobantesOfflineService;
import ec.facturacionelectronica.rec.RespuestaSolicitud;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;



import org.apache.log4j.Logger;

/**
 * https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl
 *
 * Cliente con metodos de conexion al WS Offline
 *
 * @author Gabriel Eguiguren
 */
public class EnvioWs {

    static Logger log = Logger.getLogger(EnvioWs.class.getName());
    private static RecepcionComprobantesOfflineService service;

    public static final String ESTADO_RECIBIDA = "RECIBIDA";
    public static final String ESTADO_DEVUELTA = "DEVUELTA";

    public EnvioWs(String wsdlLocation) throws MalformedURLException, WebServiceException {
        URL url = new URL(wsdlLocation);
        QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
        service = new RecepcionComprobantesOfflineService(url, qname);
    }

    public static final Object webService(String wsdlLocation) {
        try {
            QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
            URL url = new URL(wsdlLocation);
            service = new RecepcionComprobantesOfflineService(url, qname);
            return null;
        } catch (MalformedURLException ex) {
            log.error(ex);
            return ex;
        } catch (WebServiceException ws) {
            log.error(ws);
            return ws;
        }
    }

    /**
     * Consume el WS para envio de un comprobante
     * 
     * @param xmlFile
     * @return
     */
    public RespuestaSolicitud enviarComprobante(File xmlFile) {

        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();
            response = port.validarComprobante(FileUtils.archivoToByte(xmlFile));

        } catch (Exception e) {
            log.error(e);
            response = new RespuestaSolicitud();
            response.setEstado(e.getMessage());
            return response;
        }

        return response;
    }

    /**
     * Envia el comprobante al WS y devuelve la respuesta del estado del
     * comprobante
     *
     * @param archivo
     * @return
     */
    public static RespuestaSolicitud obtenerRespuestaEnvio(File archivo, String urlWsdl) {

        RespuestaSolicitud respuesta = new RespuestaSolicitud();
        EnvioWs cliente = null;
        try {
            cliente = new EnvioWs(urlWsdl);
        } catch (Exception ex) {
            log.error(ex);
            respuesta.setEstado(ex.getMessage());
            return respuesta;
        }
        respuesta = cliente.enviarComprobante(archivo);

        return respuesta;
    }

    /**
     * Consume el WS para envio de comprobantes por lotes
     * @param xml
     * @return
     */
    public RespuestaSolicitud enviarComprobanteLotes(byte[] xml) {

        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();

            response = port.validarComprobante(xml);

        } catch (Exception e) {
            log.error(e);
            response = new RespuestaSolicitud();
            response.setEstado(e.getMessage());
            return response;
        }
        return response;
    }

    /**
     * Obtiene un String con todos los motivos de rechazo de una respuesta
     *
     * @param respuesta
     * @return
     */
    public static String obtenerMensajeRespuesta(RespuestaSolicitud respuesta) {

        StringBuilder mensajeDesplegable = new StringBuilder();
        if (respuesta.getEstado().equals(EnvioWs.ESTADO_DEVUELTA) == true) {

            RespuestaSolicitud.Comprobantes comprobantes = (RespuestaSolicitud.Comprobantes) respuesta.getComprobantes();
            for (Comprobante comp : comprobantes.getComprobante()) {
                mensajeDesplegable.append(comp.getClaveAcceso());
                mensajeDesplegable.append("\n");
                for (Mensaje m : comp.getMensajes().getMensaje()) {
                    mensajeDesplegable.append(m.getMensaje()).append(" :\n");
                    mensajeDesplegable.append(m.getInformacionAdicional() != null ? m.getInformacionAdicional() : "");
                    mensajeDesplegable.append("\n");
                }
                mensajeDesplegable.append("\n");
            }

        }
        return mensajeDesplegable.toString();
    }

}
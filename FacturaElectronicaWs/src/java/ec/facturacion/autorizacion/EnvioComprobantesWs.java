/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.autorizacion;

import ec.gob.sri.comprobantes.ws.Comprobante;
import ec.gob.sri.comprobantes.ws.MensajeXml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import ec.gob.sri.comprobantes.ws.RecepcionComprobantes;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesService;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import org.apache.log4j.Logger;

/**
 * https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl
 * https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl
 *
 *
 * Cliente con metodos de conexion al WS
 *
 * @author Gabriel Eguiguren
 */
public class EnvioComprobantesWs {

    static Logger log = Logger.getLogger(EnvioComprobantesWs.class.getName());
    private static RecepcionComprobantesService service;

    public static final String ESTADO_RECIBIDA = "RECIBIDA";
    public static final String ESTADO_DEVUELTA = "DEVUELTA";

    public EnvioComprobantesWs(String wsdlLocation) throws MalformedURLException, WebServiceException {
        URL url = new URL(wsdlLocation);
        QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService");
        service = new RecepcionComprobantesService(url, qname);
    }

    public static final Object webService(String wsdlLocation) {
        try {
            QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService");
            URL url = new URL(wsdlLocation);
            service = new RecepcionComprobantesService(url, qname);
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
     * Consume el WS para env�o de un componente
     *
     * @param ruc
     * @param xmlFile
     * @param tipoComprobante
     * @param versionXsd
     * @param url
     * @return
     */
    public RespuestaSolicitud enviarComprobante(File xmlFile) {

        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantes port = service.getRecepcionComprobantesPort();
            response = port.validarComprobante(archivoToByte(xmlFile));

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
     * @param urlWsdl
     * @return
     */
    public static RespuestaSolicitud obtenerRespuestaEnvio(File archivo, String urlWsdl) {

        RespuestaSolicitud respuesta = new RespuestaSolicitud();
        EnvioComprobantesWs cliente = null;
        try {
            cliente = new EnvioComprobantesWs(urlWsdl);
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
     *
     * @param ruc
     * @param xml
     * @param tipoComprobante
     * @param versionXsd
     * @param url
     * @return
     */
    public RespuestaSolicitud enviarComprobanteLotes(byte[] xml) {

        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantes port = service.getRecepcionComprobantesPort();

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
        if (respuesta.getEstado().equals(EnvioComprobantesWs.ESTADO_DEVUELTA) == true) {

            RespuestaSolicitud.Comprobantes comprobantes = (RespuestaSolicitud.Comprobantes) respuesta.getComprobantes();
            for (Comprobante comp : comprobantes.getComprobante()) {
                mensajeDesplegable.append(comp.getClaveAcceso());
                mensajeDesplegable.append("\n");
                for (MensajeXml m : comp.getMensajes().getMensaje()) {
                    mensajeDesplegable.append(m.getMensaje()).append(" :\n");
                    mensajeDesplegable.append(m.getInformacionAdicional() != null ? m.getInformacionAdicional() : "");
                    mensajeDesplegable.append("\n");
                }
                mensajeDesplegable.append("\n");
            }

        }
        return mensajeDesplegable.toString();
    }

    /**
     * Transforma un archivo (File) a bytes
     *
     * @param file
     * @return
     * @throws IOException
     */
    public byte[] archivoToByte(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                //TODO LOGGER
            }
        }

        return buffer;
    }

}

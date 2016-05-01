/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.autorizacion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.thoughtworks.xstream.XStream;

import ec.facturacion.firmaxades.util.xml.FileUtils;
import ec.facturacion.firmaxades.util.xml.XStreamUtil;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantes;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantesService;
import ec.gob.sri.comprobantes.ws.aut.MensajeXml;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import ec.gob.sri.comprobantes.ws.aut.RespuestaLote;
import org.apache.log4j.Logger;

/**
 * Cliente con métodos de conexión al WS de Autorización
 *
 * @author Gabriel Eguiguren
 */
public class AutorizacionComprobantesWs {

    static Logger log = Logger.getLogger(AutorizacionComprobantesWs.class.getName());

    private AutorizacionComprobantesService service;
    public static final String ESTADO_AUTORIZADO = "AUTORIZADO";
    public static final String ESTADO_NO_AUTORIZADO = "NO AUTORIZADO";
    public static final String AUTORIDAD_CERT_NO_DISPONIBLE = "61";
    public static final String COD_UTF8 = "UTF-8";
    public static final int INTENTOS_RESPUESTA_AUTORIZACION_WS = 5;

    public AutorizacionComprobantesWs(String wsdlLocation) {
        try {
            this.service = new AutorizacionComprobantesService(new URL(wsdlLocation),
                    new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * Consume el WS para la recepción de la autorización de un comprobante
     *
     * @param claveDeAcceso
     * @param url
     * @return
     */
    public RespuestaComprobante llamadaWSAutorizacionInd(String claveDeAcceso) {

        RespuestaComprobante response = null;
        try {
            AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
            response = port.autorizacionComprobante(claveDeAcceso);

        } catch (Exception e) {
            log.error(e);
            return response;
        }

        return response;
    }

    /**
     * Consume el WS para envío de comprobantes por lotes
     *
     * @param ruc
     * @param xml
     * @param tipoComprobante
     * @param versionXsd
     * @param url
     * @return
     */
    public RespuestaLote llamadaWsAutorizacionLote(String claveDeAcceso) {

        RespuestaLote response = null;
        try {
            AutorizacionComprobantes port = service
                    .getAutorizacionComprobantesPort();
            response = port.autorizacionComprobanteLote(claveDeAcceso);

        } catch (Exception e) {
            log.error(e);
            return response;
        }
        return response;
    }

    /**
     * Realiza la llamada al WS de autorizacion y graba la respuesta en el
     * directorio correspondiente
     *
     * @param claveDeAcceso
     * @param nombreArchivo
     * @param tipoAmbiente
     * @return
     */
    public static RespuestaAutorizacion autorizarComprobanteIndividual(String claveDeAcceso,
            String nombreArchivo, String urlWsdlAutorizacion) {
        StringBuilder mensaje = new StringBuilder();
        AutorizacionComprobantesWs.RespuestaAutorizacion resp = new AutorizacionComprobantesWs.RespuestaAutorizacion();

        try {

            String dirAutorizados = ".";
            String dirNoAutorizados = ".";

            RespuestaComprobante respuesta = null;

            // llama al WS de autorizacion y hace N intentos hasta que llegue
            // lleno el objeto RespuestaComprobante
            for (int i = 0; i < INTENTOS_RESPUESTA_AUTORIZACION_WS; i++) {
                respuesta = new AutorizacionComprobantesWs(urlWsdlAutorizacion)
                        .llamadaWSAutorizacionInd(claveDeAcceso);

                if (respuesta.getAutorizaciones().getAutorizacion().isEmpty() == false) {
                    break;
                }
                Thread.currentThread().sleep(500);
            }

            if (respuesta != null) {
                resp.respuesta = respuesta;

                int i = 0;
                for (Autorizacion item : respuesta.getAutorizaciones()
                        .getAutorizacion()) {
                    mensaje.append(item.getEstado());

                    // se modifica para que el comprobante vaya dentro de un
                    // CDATA
                    item.setComprobante("<![CDATA[" + item.getComprobante()
                            + "]]>");

                    XStream xstream = XStreamUtil.getRespuestaXStream();
                    Writer writer = null;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    writer = new OutputStreamWriter(outputStream, COD_UTF8);
                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

                    xstream.toXML(item, writer);
                    String xmlAutorizacion = outputStream.toString(COD_UTF8);

                    // logica de copiado del archivo a directorio segunfue autorizado o no
                    if (i == 0 && item.getEstado().equals(ESTADO_AUTORIZADO)) {
                        FileUtils.stringToArchivo(dirAutorizados
                                + File.separator + nombreArchivo,
                                xmlAutorizacion);

                    } else if (item.getEstado().equals(ESTADO_NO_AUTORIZADO)) {
                        FileUtils.stringToArchivo(dirNoAutorizados
                                + File.separator + nombreArchivo,
                                xmlAutorizacion);
                        mensaje.append("|" + obtieneMensajesAutorizacion(item));

                        verificarOCSP(item);

                        break; // 06-02-12 se solicita que solo se
                        // procese(grabe) el primer item del array de
                        // autorizaciones
                    }
                    i++;
                }

            }
            // 09-02-2012 Moverlos a directorio transmitidos sin respuesta
            if (respuesta == null
                    || respuesta.getAutorizaciones().getAutorizacion()
                    .isEmpty() == true) {
                mensaje.append("TRANSMITIDO SIN RESPUESTA| ERROR_TRANSMITIDO");

                // TODO
                String dirFirmados = ".";
                // TODO
                String dirTransmitidos = ".";

                File transmitidos = new File(dirTransmitidos);
                if (transmitidos.exists() == false) {
                    new File(dirTransmitidos).mkdir();
                }

                File archivoFirmado = new File(new File(dirFirmados),
                        nombreArchivo);
                if (FileUtils.copiarArchivo(archivoFirmado, transmitidos.getPath()
                        + File.separator + nombreArchivo) == false) {
                    // TODO mensaje.append(Mensajes.ERROR_MOVER_TRANSM);
                } else {
                    //archivoFirmado.delete();
                }
            }

        } catch (Exception ex) {
            log.error(ex);
        }
        resp.novedades = mensaje.toString();
        return resp;

    }

    /**
     * Obtiene la clave de accesso presente en una clase tipo Autorizacion
     *
     * @param item
     * @return
     */
    public static String obtieneClaveAccesoAutorizacion(Autorizacion item) {
        String claveAcceso = null;

        String xmlAutorizacion = XStreamUtil.getRespuestaLoteXStream().toXML(
                item);
        File archivoTemporal = new File("temp.xml");

        FileUtils.stringToArchivo(archivoTemporal.getPath(), xmlAutorizacion);
        String contenidoXML = FileUtils.decodeArchivoBase64(archivoTemporal
                .getPath());

        if (contenidoXML != null) {
            FileUtils.stringToArchivo(archivoTemporal.getPath(), contenidoXML);
            claveAcceso = FileUtils.obtenerValorXML(archivoTemporal,
                    "/*/infoTributaria/claveAcceso");
            //archivoTemporal.delete();
        }

        return claveAcceso;
    }

    /**
     *
     *
     * @param claveDeAcceso
     * @param dirAutorizados
     * @param nombreArchivoLote
     * @return
     * @throws InterruptedException
     */
    public static List<Autorizacion> autorizarComprobanteLote(
            String claveDeAcceso, String nombreArchivoLote, int timeout,
            int cantidadArchivos, String tipoAmbiente, String urlWsdlAutorizacion) {
        List<Autorizacion> autorizaciones = new ArrayList();
        RespuestaLote respuestaAutorizacion = null;
        try {

            // TODO
            String dirAutorizados = ".";
            String dirRechazados = ".";
            String dirFirmados = ".";

            // time out general que depende del numero de archivos enviados
            Thread.currentThread().sleep(timeout * cantidadArchivos * 1000);

            // llama al WS de autorizacion y realiza varios intentos hasta que
            // el vector de autorizaciones venga lleno
            for (int i = 0; i < INTENTOS_RESPUESTA_AUTORIZACION_WS; i++) {
                respuestaAutorizacion = new AutorizacionComprobantesWs(urlWsdlAutorizacion)
                        .llamadaWsAutorizacionLote(claveDeAcceso);

                if (respuestaAutorizacion.getAutorizaciones().getAutorizacion()
                        .isEmpty() == false) {
                    break;
                }
                Thread.currentThread().sleep(500);
            }

            String comprobantesProcesados = respuestaAutorizacion
                    .getNumeroComprobantesLote();
            for (Autorizacion item : respuestaAutorizacion.getAutorizaciones()
                    .getAutorizacion()) {

                item.setComprobante("<![CDATA[" + item.getComprobante() + "]]>");
                String claveAcceso = obtieneClaveAccesoAutorizacion(item);

                XStream xstream = XStreamUtil.getRespuestaLoteXStream();
                Writer writer = null;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                writer = new OutputStreamWriter(outputStream, COD_UTF8);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

                xstream.toXML(item, writer);

                String nombreArchivoConRespuesta = claveAcceso + ".xml";
                String xmlAutorizacion = outputStream.toString(COD_UTF8);

                if (item.getEstado().equals("AUTORIZADO")) {
                    FileUtils.stringToArchivo(dirAutorizados + File.separator
                            + nombreArchivoConRespuesta, xmlAutorizacion);
                } else {
                    FileUtils.stringToArchivo(dirRechazados + File.separator
                            + nombreArchivoConRespuesta, xmlAutorizacion);
                }

                File archivoABorrar = new File(dirFirmados + File.separator
                        + nombreArchivoConRespuesta);
                if (archivoABorrar.exists() == true) {
                    //archivoABorrar.delete();
                }

                item.setEstado(nombreArchivoConRespuesta + "|"
                        + item.getEstado() + "|" + comprobantesProcesados);
                autorizaciones.add(item);

            }

        } catch (Exception ex) {
            log.error(ex);
        }
        return autorizaciones;

    }

    /**
     * Crea un string con todos los mensajes de la autorizacion
     *
     * @param autorizacion
     * @return
     */
    public static String obtieneMensajesAutorizacion(Autorizacion autorizacion) {
        StringBuilder mensaje = new StringBuilder();
        for (MensajeXml m : autorizacion.getMensajes().getMensaje()) {
            if (m.getInformacionAdicional() != null) {
                mensaje.append("\n" + m.getMensaje() + ": "
                        + m.getInformacionAdicional());
            } else {
                mensaje.append("\n" + m.getMensaje());
            }
        }

        return mensaje.toString();
    }

    /**
     *
     * @param autorizacion
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static boolean verificarOCSP(Autorizacion autorizacion)
            throws SQLException, ClassNotFoundException {
        boolean respuesta = true;

        for (MensajeXml m : autorizacion.getMensajes().getMensaje()) {
            if (m.getIdentificador().equals(AUTORIDAD_CERT_NO_DISPONIBLE)) {
                log.info("No se puede validar el certificado digital."
                        + "\n Desea emitir en contingencia?");
                respuesta = false;
            }
        }
        return respuesta;
    }

    public static class RespuestaAutorizacion {

       public  RespuestaComprobante respuesta;
       public  String novedades;

    }

}

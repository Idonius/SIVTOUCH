package ec.facturacion.service;

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
import ec.facturacionelectronica.aut.Autorizacion;
import ec.facturacionelectronica.aut.AutorizacionComprobantesOffline;
import ec.facturacionelectronica.aut.AutorizacionComprobantesOfflineService;
import ec.facturacionelectronica.aut.Mensaje;
import ec.facturacionelectronica.aut.RespuestaLote;
import java.net.MalformedURLException;
import org.apache.log4j.Logger;

/**
 * Cliente con métodos de conexión al WS de Autorización
 *
 * @author Gabriel Eguiguren
 */
public class AutorizacionWs {

    static Logger log = Logger.getLogger(AutorizacionWs.class.getName());

    private AutorizacionComprobantesOfflineService service;
    public static final String ESTADO_AUTORIZADO = "AUTORIZADO";
    public static final String ESTADO_NO_AUTORIZADO = "NO AUTORIZADO";
    public static final String AUTORIDAD_CERT_NO_DISPONIBLE = "61";
    public static final String COD_UTF8 = "UTF-8";
    public static final int INTENTOS_RESPUESTA_AUTORIZACION_WS = 5;

    public AutorizacionWs(String wsdlLocation) {
        try {
            this.service = new AutorizacionComprobantesOfflineService(new URL(wsdlLocation),
                    new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService"));

        } catch (MalformedURLException ex) {
            log.error(ex);
        }
    }

    /**
     * Consume el WS para la recepción de la autorización de un comprobante
     *
     * @param claveDeAcceso
     * @return un objeto tipo RespuestaComprobante propio del WS
     */
    public ec.facturacionelectronica.aut.RespuestaComprobante llamadaWSAutorizacionInd(String claveDeAcceso) {

        ec.facturacionelectronica.aut.RespuestaComprobante response = null;
        try {
            AutorizacionComprobantesOffline port = service.getAutorizacionComprobantesOfflinePort();
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
     *
     * @param claveDeAcceso a ser consultada en el WS de autorizacion
     * @return un objeto tipo RespuestaLote propio del WS para ser procesado por
     * el desarrollador
     */
    public RespuestaLote llamadaWsAutorizacionLote(String claveDeAcceso) {

        RespuestaLote response = null;
        try {
            AutorizacionComprobantesOffline port = service.getAutorizacionComprobantesOfflinePort();
            response = port.autorizacionComprobanteLote(claveDeAcceso);

        } catch (Exception e) {
            log.error(e);
            return response;
        }
        return response;
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
     * @TODO Funcion que consulta un lote de varios tipos de comprobantes al WS de
     * autorizacion
     *
     * @param claveDeAcceso
     * @param nombreArchivoLote
     * @param timeout
     * @param cantidadArchivos
     * @param tipoAmbiente
     * @param urlWsdlAutorizacion
     * @return
     */
    public static List<Autorizacion> autorizarComprobanteLote(
            String claveDeAcceso, String nombreArchivoLote, int timeout,
            int cantidadArchivos, String tipoAmbiente, String urlWsdlAutorizacion) {
        List<Autorizacion> autorizaciones = new ArrayList();
        RespuestaLote respuestaAutorizacion = null;
        try {

            // TODO aMEJORAR DIRECTORIOS
            String dirAutorizados = ".";
            String dirRechazados = ".";
            String dirFirmados = ".";

            // time out general que depende del numero de archivos enviados
            Thread.currentThread().sleep(timeout * cantidadArchivos * 1000);

            // llama al WS de autorizacion y realiza varios intentos hasta que
            // el vector de autorizaciones venga lleno
            for (int i = 0; i < INTENTOS_RESPUESTA_AUTORIZACION_WS; i++) {
                respuestaAutorizacion = new AutorizacionWs(urlWsdlAutorizacion)
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
        for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
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

        for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
            if (m.getIdentificador().equals(AUTORIDAD_CERT_NO_DISPONIBLE)) {
                log.info("No se puede validar el certificado digital."
                        + "\n Desea emitir en contingencia?");
                respuesta = false;
            }
        }
        return respuesta;
    }

}

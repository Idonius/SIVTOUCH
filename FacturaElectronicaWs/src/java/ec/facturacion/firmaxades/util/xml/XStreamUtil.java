package ec.facturacion.firmaxades.util.xml;

import java.io.Writer;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.MensajeXml;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import ec.gob.sri.comprobantes.ws.aut.RespuestaLote;

/**
 *
 * @author marco zaragocin, Gabriel Eguiguren
 * 
 */
public class XStreamUtil {

    /**
     * Crea el objeto basado en el modelo para el envío por lotes
     * 
     * @return 
     */
    public static XStream getLoteXStream() {

        XStream xstream = new XStream(
                new XppDriver() {

                    @Override
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new PrettyPrintWriter(out) {

                            @Override
                            protected void writeText(QuickWriter writer, String text) {
                                writer.write(text);
                            }
                        };
                    }
                });

        xstream.alias("lote", LoteXml.class);
        xstream.alias("comprobante", ComprobanteXml.class);

        //convierten las clases 
        xstream.registerConverter(new ComprobanteXmlConverter());

        return xstream;
    }

    /**
     * Crea el objeto basado en el modelo para el envío por lotes
     * 
     * @return 
     */
    public static XStream getRespuestaXStream() {

        XStream xstream = new XStream(
                new XppDriver() {

                    @Override
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new PrettyPrintWriter(out) {

                            @Override
                            protected void writeText(QuickWriter writer, String text) {
                                writer.write(text);
                            }
                        };
                    }
                });

        xstream.alias("respuesta", RespuestaComprobante.class);
        xstream.alias("autorizacion", Autorizacion.class);
        xstream.alias("fechaAutorizacion", XMLGregorianCalendarImpl.class);
        xstream.alias("mensaje", MensajeXml.class);
        xstream.registerConverter(new RespuestaDateConverter());

        return xstream;
    }

    /**
     * Crea el objeto basado en el modelo para el envío por lotes
     * 
     * @return 
     */
    public static XStream getRespuestaLoteXStream() {

        XStream xstream = new XStream(
                new XppDriver() {

                    @Override
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new PrettyPrintWriter(out) {

                            @Override
                            protected void writeText(QuickWriter writer, String text) {
                                writer.write(text);
                            }
                        };
                    }
                });

        xstream.alias("respuesta", RespuestaLote.class);
        xstream.alias("autorizacion", Autorizacion.class);
        xstream.alias("fechaAutorizacion", XMLGregorianCalendarImpl.class);
        xstream.alias("mensaje", MensajeXml.class);
        xstream.registerConverter(new RespuestaDateConverter());

        return xstream;
    }
}

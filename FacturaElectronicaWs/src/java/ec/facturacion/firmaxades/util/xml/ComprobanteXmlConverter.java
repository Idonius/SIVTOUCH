
package ec.facturacion.firmaxades.util.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;



/**
 * Crea el conversor de la clase ComprobanteXml
 *
 * @author Gabriel Eguiguren
 */
public class ComprobanteXmlConverter implements Converter {

    public boolean canConvert(Class clazz) {
        return clazz.equals(ComprobanteXml.class);
    }

    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
        ComprobanteXml i = (ComprobanteXml) o;
//        writer.addAttribute("tipo", i.getTipo());
//        writer.addAttribute("version", i.getVersion());
        
        writer.setValue(i.getFileXML());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        ComprobanteXml item = new ComprobanteXml();
        item.setTipo(reader.getAttribute("tipo"));
        item.setVersion(reader.getAttribute("version"));
        item.setFileXML(reader.getValue());

        return item;
    }
}
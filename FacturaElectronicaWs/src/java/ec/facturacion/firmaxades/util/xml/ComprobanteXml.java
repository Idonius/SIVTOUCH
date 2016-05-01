
package ec.facturacion.firmaxades.util.xml;

/**
 *
 * @author Gabriel Eguiguren
 */
public class ComprobanteXml {

    private String tipo;
    private String version;
    private String fileXML;

    public String getFileXML() {
        return fileXML;
    }

    public void setFileXML(String fileXML) {
        this.fileXML = "<![CDATA[" + fileXML + "]]>" ;
       
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

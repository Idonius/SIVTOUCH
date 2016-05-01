package ec.facturacion.firmaxades.util.xml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Eguiguren
 */
public class LoteXml {

    private String version;
    private String claveAcceso;
    private String ruc;
    private List<ComprobanteXml> comprobantes;

    public LoteXml() {
        this.comprobantes = new ArrayList<ComprobanteXml>();
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public List<ComprobanteXml> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<ComprobanteXml> comprobantes) {
        this.comprobantes = comprobantes;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}

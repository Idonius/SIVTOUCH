/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.app;

import java.io.File;

public class ComprobanteEnviado {

    File archivo;
    String numAutorizacion;
    String estadoComprobante;
    String novedades;

    public ComprobanteEnviado() {
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public String getEstadoComprobante() {
        return estadoComprobante;
    }

    public void setEstadoComprobante(String estadoComprobante) {
        this.estadoComprobante = estadoComprobante;
    }

    public String getNovedades() {
        return novedades;
    }

    public void setNovedades(String novedades) {
        this.novedades = novedades;
    }

}

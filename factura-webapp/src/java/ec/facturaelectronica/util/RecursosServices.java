/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.util;

import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author desarrollotic
 */
public class RecursosServices {

    public ResourceBundle recurso = ResourceBundle.getBundle("ec.facturaelectronica.resources.etiquetas");
    public ResourceBundle certificado = ResourceBundle.getBundle("ec.facturaelectronica.resources.certificados");
    
    public Logger LOG = LogManager.getLogger("HelloWorld");

    public ResourceBundle getRecurso() {
        return recurso;
    }

    public void setRecurso(ResourceBundle recurso) {
        this.recurso = recurso;
    }

    public ResourceBundle getCertificado() {
        return certificado;
    }

    public void setCertificado(ResourceBundle certificado) {
        this.certificado = certificado;
    }
}

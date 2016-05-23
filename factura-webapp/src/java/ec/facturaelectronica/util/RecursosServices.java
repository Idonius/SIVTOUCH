/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.util;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author desarrollotic
 */
public class RecursosServices {

    public ResourceBundle recurso = ResourceBundle.getBundle("ec.facturaelectronica.resources.etiquetas");
    public ResourceBundle certificado = ResourceBundle.getBundle("ec.facturaelectronica.resources.certificados");
    
    public Logger LOG = LogManager.getLogger("FacturaElectronica");

    public ResourceBundle getRecurso() {
        return recurso;
    }
    
    protected void errorMessages(String mensaje, String detalle, String objeto){
        FacesMessage msg;
        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, detalle);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update(objeto);                   
    }
    
    protected void warnMessages(String mensaje, String detalle, String objeto){
        FacesMessage msg;
        msg = new FacesMessage(FacesMessage.SEVERITY_WARN, mensaje, detalle);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update(objeto);                   
    }
    
    protected void infoMessages(String mensaje, String detalle, String objeto){
        FacesMessage msg;
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, detalle);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update(objeto);                   
    }
    
    protected ExternalContext getContext() throws IOException{
        return FacesContext.getCurrentInstance().getExternalContext();
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

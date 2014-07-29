/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.PerfilDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author desarrollotic
 */
@ManagedBean(name = "perfilesBean")
@ViewScoped
public class PerfilesBean extends RecursosServices implements Serializable {

    /**
     * Creates a new instance of PerfilesBean
     */
    @EJB
    AdministracionService admService;
    private String nombre;
    private List<Perfil> listaPerfiles;
    private List<Perfil> filterPerfiles;
    private Perfil selectedPerfil;
    private PerfilDataTableModel perfilModel;
    private Perfil perfil;

    public PerfilesBean() {
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public List<Perfil> getFilterPerfiles() {
        return filterPerfiles;
    }

    public void setFilterPerfiles(List<Perfil> filterPerfiles) {
        this.filterPerfiles = filterPerfiles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Perfil> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(List<Perfil> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    public Perfil getSelectedPerfil() {
        return selectedPerfil;
    }

    public void setSelectedPerfil(Perfil selectedPerfil) {
        this.selectedPerfil = selectedPerfil;
    }

    public PerfilDataTableModel getPerfilModel() {
        return perfilModel;
    }

    public void setPerfilModel(PerfilDataTableModel perfilModel) {
        this.perfilModel = perfilModel;
    }

    @PostConstruct
    public void initBean() {
        FacesMessage msg;

        try {
            perfil = null;
            nombre = "";
            listaPerfiles = admService.getPerfiles();
            perfilModel = new PerfilDataTableModel(listaPerfiles);

        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("perfiles.header"), ex.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void nuevo() {

        nombre = "";
        perfil=null;
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
    }

    public void cancelar() {

        nombre = "";

        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");

    }

    public void editar() {

        if (selectedPerfil != null) {
            perfil = selectedPerfil;
            nombre = perfil.getNombrePerfil();

            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
        }
    }

    public void eliminar() {

        if (selectedPerfil != null) {
            perfil = selectedPerfil;

            RequestContext.getCurrentInstance().execute("PF('confirm').show()");
        }
    }

    public void registrar() {

        FacesMessage msg;

        try {

            if (perfil == null) {
                perfil = new Perfil();
            }

            perfil.setNombrePerfil(nombre);

            if (perfil.getIdPerfil() == null) {

                admService.guardarPerfil(perfil);
            } else {
                admService.actualizarPerfil(perfil);
            }

            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
            initBean();

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("perfiles.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (ServicesException ex) {
            perfil = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("perfiles.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (Exception ex) {
            perfil = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("perfiles.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void desactivar() {
        FacesMessage msg;

        if (perfil != null) {
            try {
                admService.eliminarPerfil(perfil);
                initBean();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("perfiles.header"), recurso.getString("perfiles.editar.eliminar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
                RequestContext.getCurrentInstance().update("form:growl");

            } catch (ServicesException ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("perfiles.header"), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("perfiles.header"), recurso.getString("perfiles.editar.elimimar.mensajer.seleccion"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }
}

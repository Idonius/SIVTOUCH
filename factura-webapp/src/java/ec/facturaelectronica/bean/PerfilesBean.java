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
import ec.facturaelectronica.vo.PerfilVo;
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

    private List<Perfil> listaPerfiles;

    private Perfil selectedPerfil;
    private PerfilDataTableModel perfilModel;
    private Perfil perfil;

    private PerfilVo perfilVo;

    public PerfilesBean() {
    }

    @PostConstruct
    public void initBean() {
        perfilVo = new PerfilVo();
        cargarPerfiles();
        perfilVo.desactivarEdicion();
    }

    private void cargarPerfiles() {
        FacesMessage msg;

        try {
            perfil = null;
            listaPerfiles = admService.getPerfiles();
            perfilModel = new PerfilDataTableModel(listaPerfiles);

        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("perfiles.header"), ex.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void nuevo() {

        perfilVo = new PerfilVo();
        perfil = null;
        perfilVo.activarEdicion();
    }

    public void cancelar() {
        perfil = null;
        perfilVo.desactivarEdicion();

    }

    public void editar() {
        if (selectedPerfil != null) {
            perfil = selectedPerfil;
            perfilVo.setNombre(perfil.getNombrePerfil());
            perfilVo.activarEdicion();

        }
    }

    public void eliminar(Perfil perfilSeleccionado) {
        FacesMessage msg;
        System.out.println(perfilSeleccionado);
        perfil = perfilSeleccionado;
        try {
            admService.eliminarPerfil(perfil);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("perfiles.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cargarPerfiles();
        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("perfiles.header"), ex.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void registrar() {

        FacesMessage msg;

        try {

            if (perfil == null) {
                perfil = new Perfil();
            }

            perfil.setNombrePerfil(perfilVo.getNombre());

            if (perfil.getIdPerfil() == null) {

                admService.guardarPerfil(perfil);
            } else {
                admService.actualizarPerfil(perfil);
            }

            perfilVo.desactivarEdicion();
            cargarPerfiles();

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

    /**
     * @return the perfilVo
     */
    public PerfilVo getPerfilVo() {
        return perfilVo;
    }

    /**
     * @param perfilVo the perfilVo to set
     */
    public void setPerfilVo(PerfilVo perfilVo) {
        this.perfilVo = perfilVo;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
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

}

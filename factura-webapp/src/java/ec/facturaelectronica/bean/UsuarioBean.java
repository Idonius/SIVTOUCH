/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.bean.service.UtilBeanService;
import ec.facturaelectronica.constantes.FacturaConstantes;
import ec.facturaelectronica.datatable.model.UsuarioDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.util.RecursosServices;
import ec.facturaelectronica.vo.UsuarioVo;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author desarrollotic
 */
@ManagedBean(name = "usuarioBean")
@ViewScoped
public class UsuarioBean extends RecursosServices implements Serializable {

    /**
     * Creates a new instance of UsuariosBean
     */
    @EJB
    AdministracionService admService;

    @ManagedProperty("#{utilBeanService}")
    private UtilBeanService servicioUtil;

    private UsuarioVo usuarioVo;

    private Usuario selectedUsuario;
    private List<Usuario> listUsuarios;
    private List<Usuario> filterUsuarios;
    private UsuarioDataTableModel usuarioModel;
    private List<Empresa> listEmpresas;

    private List<Perfil> listPerfiles;

    private Usuario usuario;

    public UsuarioBean() {

    }

    @PostConstruct
    public void initBean() {
        usuarioVo = new UsuarioVo();

        usuario = null;
        usuarioVo.setRenderActionNewEdit(Boolean.TRUE);
        usuarioVo.setRenderActionClave(Boolean.TRUE);

        usuarioVo.setRenderEmpresa(Boolean.TRUE);

        try {

            listPerfiles = admService.getPerfiles();
            listEmpresas = admService.getEmpresas();
            cargarUsuarios();
            cargarListas();

        } catch (ServicesException ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarUsuarios() {
        listUsuarios = admService.getUsuarios();
        usuarioModel = new UsuarioDataTableModel(listUsuarios);
    }

    private void cargarListas() {
        usuarioVo.setListaPerfiles(servicioUtil.getItemsPerfiles(listPerfiles));
        usuarioVo.setListaEmpresas(servicioUtil.getItemsEmpresas(listEmpresas));
    }

    public void nuevo() {
        usuarioVo = new UsuarioVo();
        usuario = null;
        cargarListas();
        usuarioVo.nuevoUsuario();
    }

    public void seleccionaPerfil() {
        if (FacturaConstantes.SUPER_ADMINISTRADOR.equals(usuarioVo.getSelectedPerfil().toString())) {
            usuarioVo.setRenderEmpresa(Boolean.FALSE);
            usuarioVo.setSelectedEmpresa(null);
        } else {
            usuarioVo.setRenderEmpresa(Boolean.TRUE);
        }

    }

    public void registrar() {

        FacesMessage msg;

        try {

            if (usuario == null) {
                usuario = new Usuario();
            }

            if (!usuarioVo.getClave().equals(usuarioVo.getRclave())) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), recurso.getString("usuarios.editar.mensaje.claves"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                return;
            }

            usuario.setNombreUsuario(usuarioVo.getNombre());
            usuario.setClaveUsuario(usuarioVo.getClave());
            usuario.setIdPerfil(new Perfil(usuarioVo.getSelectedPerfil()));
            usuario.setNickUsuario(usuarioVo.getNick());
            usuario.setCargoUsuario(usuarioVo.getCargo());
            usuario.setCedulaUsuario(usuarioVo.getCedula());
            usuario.setEmailUsuario(usuarioVo.getEmail());

            if (usuarioVo.getSelectedEmpresa() != null) {
                usuario.setIdEmpresa(new Empresa(usuarioVo.getSelectedEmpresa()));

            }

            if (usuario.getIdUsuario() == null) {

                admService.guardarUsuario(usuario);
            } else {

                admService.actualizarUsuario(usuario);
            }

            cargarUsuarios();
            usuarioVo.initUsuario();

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("usuarios.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage("form:growl", msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (ServicesException ex) {
            usuario = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (Exception ex) {
            usuario = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void cancelar() {
        usuario = null;
        usuarioVo.initUsuario();

    }

    public void cancelarPassword() {

        usuario = null;
        usuarioVo.initUsuario();

    }

    public void resetClave() {

        FacesMessage msg;

        try {

            if (!usuarioVo.getNclave().equals(usuarioVo.getRnclave())) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), recurso.getString("usuarios.editar.mensaje.claves"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                return;
            }

            admService.resetClaveUsuario(usuario, usuarioVo.getNclave());

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("usuarios.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cancelarPassword();

        } catch (ServicesException ex) {
            usuario = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (Exception ex) {
            usuario = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void editar() {
        usuarioVo = new UsuarioVo();
        cargarListas();
        if (selectedUsuario != null) {

            usuarioVo.setEditUser(Boolean.TRUE);
            usuario = selectedUsuario;

            usuarioVo.setNombre(usuario.getNombreUsuario());
            usuarioVo.setNick(usuario.getNickUsuario());
            usuarioVo.setSelectedPerfil(usuario.getIdPerfil().getIdPerfil());
            System.err.println(usuario.getIdEmpresa());
            if (usuario.getIdEmpresa() != null) {
                usuarioVo.setSelectedEmpresa(usuario.getIdEmpresa().getIdEmpresa());
                System.out.println(usuarioVo.getSelectedEmpresa());
            }

            usuarioVo.setClave(usuario.getClaveUsuario());
            usuarioVo.setRclave(usuario.getClaveUsuario());
            usuarioVo.setCedula(usuario.getCedulaUsuario());
            usuarioVo.setCargo(usuario.getCargoUsuario());
            usuarioVo.setEmail(usuario.getEmailUsuario());

            usuarioVo.setRenderActionNewEdit(Boolean.FALSE);

            seleccionaPerfil();
        }
    }

    public void eliminar() {

        FacesMessage msg;

        if (selectedUsuario != null) {
            usuario = selectedUsuario;

            try {
                admService.eliminarUsuario(usuario);

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("usuarios.header"), recurso.getString("editar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");

            } catch (ServicesException ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, recurso.getString("usuarios.header"), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        }
    }

    public void cambiarClave() {
        if (selectedUsuario != null) {
            usuarioVo.setEditUser(Boolean.TRUE);

            usuario = selectedUsuario;
            usuarioVo.setNombre(usuario.getNombreUsuario());
            usuarioVo.setNick(usuario.getNickUsuario());
            usuarioVo.setClave(usuario.getClaveUsuario());
            usuarioVo.setRclave(usuario.getClaveUsuario());
            usuarioVo.cambiarClave();

        }
    }

    public List<Empresa> getListEmpresas() {
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresa> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Perfil> getListPerfiles() {
        return listPerfiles;
    }

    public void setListPerfiles(List<Perfil> listPerfiles) {
        this.listPerfiles = listPerfiles;
    }

    public List<Usuario> getFilterUsuarios() {
        return filterUsuarios;
    }

    public void setFilterUsuarios(List<Usuario> filterUsuarios) {
        this.filterUsuarios = filterUsuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public List<Usuario> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuarios(List<Usuario> listUsuarios) {
        this.listUsuarios = listUsuarios;
    }

    public UsuarioDataTableModel getUsuarioModel() {
        return usuarioModel;
    }

    public void setUsuarioModel(UsuarioDataTableModel usuarioModel) {
        this.usuarioModel = usuarioModel;
    }

    /**
     * @return the usuarioVo
     */
    public UsuarioVo getUsuarioVo() {
        return usuarioVo;
    }

    /**
     * @param usuarioVo the usuarioVo to set
     */
    public void setUsuarioVo(UsuarioVo usuarioVo) {
        this.usuarioVo = usuarioVo;
    }

    /**
     * @return the servicioUtil
     */
    public UtilBeanService getServicioUtil() {
        return servicioUtil;
    }

    /**
     * @param servicioUtil the servicioUtil to set
     */
    public void setServicioUtil(UtilBeanService servicioUtil) {
        this.servicioUtil = servicioUtil;
    }

}

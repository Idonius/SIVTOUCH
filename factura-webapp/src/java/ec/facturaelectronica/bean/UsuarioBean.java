/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;


import ec.facturaelectronica.datatable.model.UsuarioDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.EmpresaListModel;
import ec.facturaelectronica.list.model.PerfilListModel;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ManagedBean(name = "usuarioBean")
@ViewScoped
public class UsuarioBean extends RecursosServices implements Serializable {

    /**
     * Creates a new instance of UsuariosBean
     */
    @EJB
    AdministracionService admService;
    private Usuario selectedUsuario;
    private List<Usuario> listUsuarios;
    private List<Usuario> filterUsuarios;
    private UsuarioDataTableModel usuarioModel;
    private List<Empresa> listEmpresas;
    private Empresa selectedEmpresa;
    private String nombre;
    private String nick;
    private String clave;
    private String cedula;
    private String cargo;
    private String email;
    private List<Perfil> listPerfiles;
    private Perfil selectedPerfil;
    private Usuario usuario;
    private String rclave;
    private boolean editUser;
    private String estadoMenUser;
    private String nclave;
    private String rnclave;

    public UsuarioBean() {
    }

 
    public List<Empresa> getListEmpresas() {
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresa> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public Empresa getSelectedEmpresa() {
        return selectedEmpresa;
    }

    public void setSelectedEmpresa(Empresa selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNclave() {
        return nclave;
    }

    public void setNclave(String nclave) {
        this.nclave = nclave;
    }

    public String getRnclave() {
        return rnclave;
    }

    public void setRnclave(String rnclave) {
        this.rnclave = rnclave;
    }

    public String getEstadoMenUser() {
        return estadoMenUser;
    }

    public void setEstadoMenUser(String estadoMenUser) {
        this.estadoMenUser = estadoMenUser;
    }

    public boolean isEditUser() {
        return editUser;
    }

    public void setEditUser(boolean editUser) {
        this.editUser = editUser;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getRclave() {
        return rclave;
    }

    public void setRclave(String rclave) {
        this.rclave = rclave;
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

    public Perfil getSelectedPerfil() {
        return selectedPerfil;
    }

    public void setSelectedPerfil(Perfil selectedPerfil) {
        this.selectedPerfil = selectedPerfil;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @PostConstruct
    public void initBean() {

        nombre = "";
        usuario = null;
        nick = "";
        clave = "";
        rclave = "";
        cedula = "";
        cargo = "";
        email = "";
        editUser = false;
        selectedPerfil = null;

        try {
            listUsuarios = admService.getUsuarios();

            listPerfiles = admService.getPerfiles();
            listEmpresas=admService.getEmpresas();
            usuarioModel = new UsuarioDataTableModel(listUsuarios);
            PerfilListModel.perfilModel = listPerfiles;
            EmpresaListModel.empresaModel=listEmpresas;

        } catch (ServicesException ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nuevo() {

        nombre = "";
        usuario = null;
        nick = "";
        clave = "";
        rclave = "";
        cedula = "";
        cargo = "";
        email = "";
        editUser = false;
        selectedPerfil = null;

        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
    }

    public void registrar() {

        FacesMessage msg;

        try {

            if (usuario == null) {
                usuario = new Usuario();
            }

            if (!clave.equals(rclave)) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), recurso.getString("usuarios.editar.mensaje.claves"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                return;
            }
            usuario.setNombreUsuario(nombre);
            usuario.setClaveUsuario(clave);
            usuario.setIdPerfil(selectedPerfil);
            usuario.setNickUsuario(nick);
            usuario.setCargoUsuario(cargo);
            usuario.setCedulaUsuario(cedula);
            usuario.setEmailUsuario(email);
            usuario.setIdEmpresa(selectedEmpresa);

            if (usuario.getIdUsuario() == null) {

                admService.guardarUsuario(usuario);
            } else {

                admService.actualizarUsuario(usuario);
            }

            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
            initBean();

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

        nombre = "";
        usuario = null;
        nick = "";
        clave = "";
        rclave = "";
        editUser = false;
        selectedPerfil = null;


        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");

    }

    public void cancelarPassword() {

        nombre = "";
        usuario = null;
        nick = "";
        clave = "";
        rclave = "";
        editUser = false;
        selectedPerfil = null;


        RequestContext.getCurrentInstance().execute("PF('ventanaClave').hide()");

    }

    public void resetClave() {

        FacesMessage msg;

        try {



            if (!nclave.equals(rnclave)) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("usuarios.header"), recurso.getString("usuarios.editar.mensaje.claves"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                return;
            }


            admService.resetClaveUsuario(usuario, nclave);

            RequestContext.getCurrentInstance().execute("PF('ventanaClave').hide()");
            initBean();

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("usuarios.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
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

    public void editar() {
        if (selectedUsuario != null) {
            editUser = true;

            usuario = selectedUsuario;
            nombre = usuario.getNombreUsuario();
            nick = usuario.getNickUsuario();
            selectedPerfil = usuario.getIdPerfil();
            selectedEmpresa=usuario.getIdEmpresa();
            clave = usuario.getClaveUsuario();
            rclave = usuario.getClaveUsuario();
            cedula = usuario.getCedulaUsuario();
            cargo = usuario.getCargoUsuario();
            email = usuario.getEmailUsuario();
            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");

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
            editUser = true;

            usuario = selectedUsuario;
            nombre = usuario.getNombreUsuario();
            nick = usuario.getNickUsuario();
            selectedPerfil = usuario.getIdPerfil();
            clave = usuario.getClaveUsuario();
            rclave = usuario.getClaveUsuario();
            RequestContext.getCurrentInstance().execute("PF('ventanaClave').show()");

        }
    }

 
 
}
 

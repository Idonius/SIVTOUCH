/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.SeguridadService;
import ec.facturaelectronica.util.RecursosServices;
import ec.facturaelectronica.vo.LoginVo;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Christian
 */
@ManagedBean(name = "loginAccessBean")
@SessionScoped
public class LoginAccessBean extends RecursosServices implements Serializable {

    /**
     * Creates a new instance of LoginAccessBean
     */
    @EJB
    SeguridadService seguridadService;

    private Usuario usuarioLogin;

    private LoginVo loginVo;

    public LoginAccessBean() {
        loginVo = new LoginVo();
    }

    public void validarUsuario() {
        FacesMessage msg;

        try {

            boolean val = seguridadService.IsValidUser(getLoginVo().getNick(), getLoginVo().getClave());

            if (val) {
                setUsuarioLogin(seguridadService.getUsuarioByNick(getLoginVo().getNick()));

                FacesContext.getCurrentInstance().getExternalContext().redirect(recurso.getString("login.url"));
            } else {

                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("login.titulo"), recurso.getString("login.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }
        } catch (ServicesException | IOException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, recurso.getString("login.titulo"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

    public void logout() {
       
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(recurso.getString("login.urllogin1"));
        } catch (IOException ex) {
            }
        //return "login.xhtml?faces-redirect=true";
    }

    /**
     * @return the loginVo
     */
    public LoginVo getLoginVo() {
        return loginVo;
    }

    /**
     * @param loginVo the loginVo to set
     */
    public void setLoginVo(LoginVo loginVo) {
        this.loginVo = loginVo;
    }

    /**
     * @return the usuarioLogin
     */
    public Usuario getUsuarioLogin() {
        return usuarioLogin;
    }

    /**
     * @param usuarioLogin the usuarioLogin to set
     */
    public void setUsuarioLogin(Usuario usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

}

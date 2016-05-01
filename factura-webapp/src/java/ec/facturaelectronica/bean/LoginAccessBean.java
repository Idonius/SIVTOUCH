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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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

    private String username;
    private String password;
    private Usuario usuarioLogin;
   

    private LoginVo loginVo;

    public LoginAccessBean() {
        loginVo = new LoginVo();
    }

    public void login() {

        FacesContext.getCurrentInstance().getExternalContext().getRequest();

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.getUsername(), this.getPassword());
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
        } catch (ServletException e) {
           
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acceso Negado, las credenciales son incorrectas",null));
           
        } catch (IOException ex) {
            Logger.getLogger(LoginAccessBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logout() {

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.html");
        } catch (IOException ex) {
        }

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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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

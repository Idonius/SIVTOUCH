/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.UsuarioService;
import ec.facturaelectronica.service.util.Util;
import ec.facturaelectronica.util.CredencialesUtils;
import ec.facturaelectronica.util.RecursosServices;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Armando
 */
@ManagedBean
@ViewScoped
public class ModificarCredencialesBean extends RecursosServices implements Serializable{
    
    @EJB
    private UsuarioService usuarioService;
    
    @ManagedProperty(value = "#{modificarCredencialesSessionBean}")
    private ModificarCredencialesSessionBean modificarCredencialesSessionBean;
    
    private Usuario usuario = null;
    private Usuario usuarioNuevo = null;
    
    @PostConstruct
    public void init(){
        try {
            usuario = new Usuario();
            usuarioNuevo = new Usuario();
            modificarCredencialesSessionBean.setToken(getContext().getRequestParameterMap().get("token"));
        } catch (IOException ex) {
            Logger.getLogger(ModificarCredencialesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void verificar() throws IOException{
        verificarToken(modificarCredencialesSessionBean.getToken());   
    }
    
    private void verificarToken(String param) throws IOException{
        usuario = usuarioService.getUsuarioPorToken(param);
        if(usuario != null && usuario.getFechaExpiracion().before(new Date())){
            errorMessages(recurso.getString("registro.mensaje.notificacion.envio.modificacion.token.vencido"), recurso.getString("registro.mensaje.notificacion.envio.modificacion.token.vencido.detalle"), "frm:growl");
            getContext().redirect("../error.jsf");
        }
    }
    
    public void datosModificarCredenciales() throws IOException{
        if (CredencialesUtils.verificar(usuarioNuevo.getClaveUsuario(), usuarioNuevo.getConfirmarClave())) {
            if(usuario == null){
                usuario = usuarioService.getUsuarioPorToken(modificarCredencialesSessionBean.getToken());
            }
            usuario.setClaveUsuario(Util.Sha256(usuarioNuevo.getClaveUsuario()));
            usuarioService.actualizarUsuario(usuario);
            usuario = new Usuario();
            infoMessages(recurso.getString("registro.mensaje.exitoso"), recurso.getString("registro.mensaje.exitoso.detalle"), "frm:growl");
        } else {
            errorMessages(recurso.getString("registro.mensaje.error"), recurso.getString("registro.mensaje.error.detalle"), "frm:growl");
        }
    }

    public Usuario getUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(Usuario usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public void cerrar() throws IOException{
        getContext().redirect("../admin/index.jsf"); 
    }

    public void setModificarCredencialesSessionBean(ModificarCredencialesSessionBean modificarCredencialesSessionBean) {
        this.modificarCredencialesSessionBean = modificarCredencialesSessionBean;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.ComprobanteService;
import ec.facturaelectronica.service.UsuarioService;
import ec.facturaelectronica.service.util.Util;
import ec.facturaelectronica.util.CredencialesUtils;
import ec.facturaelectronica.util.RecursosServices;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
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
public class RegistroBean extends RecursosServices implements Serializable{
    
    @EJB
    private ComprobanteService comprobanteService;
    @EJB
    private UsuarioService usuarioService;
    
    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginAccessBean;
    
    private String identificadorSeleccionado;
    private Boolean registro = Boolean.FALSE;
    
    private List<Comprobante> comprobantes = Collections.EMPTY_LIST;
    private Usuario usuario = null;
    private Usuario usuarioNuevo = null;
    
    @PostConstruct
    public void init(){
        usuarioNuevo = new Usuario();
    }
    
    public void buscar() throws IOException{
        obtener();
        verificarDatos();
    }
    
    private void obtener(){
        try {            
            obtenerDatos();
        }catch (ServicesException ex) {
            errorMessages(recurso.getString("registro.header"), ex.getMessage(), "fRegistros:growl");
        }        
    }
    
    private void obtenerDatos() throws ServicesException {
        comprobantes = comprobanteService.obtenerComprobantePorIdentificadorComprobanteList(identificadorSeleccionado);
        usuario = usuarioService.getUsuarioPorCedula(identificadorSeleccionado);    
    }    
    
    private void verificarDatos() throws IOException{      
        this.registro = comprobantes.isEmpty() && usuario == null ? Boolean.TRUE : Boolean.FALSE;
    }
    
    public void registrar(){
        if(usuarioNuevo.getClaveUsuario().equals(usuarioNuevo.getConfirmarClave())){
            Catalogo catalogo = new Catalogo(EstadosGeneralesEnum.Activo.getOrden());
            usuarioNuevo.setCedulaUsuario(identificadorSeleccionado);
            usuarioNuevo.setClaveUsuario(Util.Sha256(usuarioNuevo.getClaveUsuario()));
            usuarioNuevo.setIdEstadoCatalogo(catalogo);
            usuarioService.crearUsuario(usuarioNuevo);
            usuarioNuevo = new Usuario();
            infoMessages(recurso.getString("registro.mensaje.exitoso"), recurso.getString("registro.mensaje.perfil.exitoso.detalle"), "fRegistros:growl");
        }else{
            errorMessages(recurso.getString("registro.mensaje.error"), recurso.getString("registro.mensaje.perfil.error"), "fRegistros:growl");
        }
    }
    
    public void modificarCredenciales(){
        if(CredencialesUtils.verificar(usuarioNuevo.getClaveUsuario(), usuarioNuevo.getConfirmarClave())){
            Usuario user = usuarioService.getUsuarioPorCedula(identificadorSeleccionado);
            user.setClaveUsuario(Util.Sha256(usuarioNuevo.getClaveUsuario()));
            usuarioService.actualizarUsuario(user);
            usuarioNuevo = new Usuario();
            infoMessages(recurso.getString("registro.mensaje.exitoso"), recurso.getString("registro.mensaje.exitoso.detalle"), "fRegistros:growl");
        }else{
            errorMessages(recurso.getString("registro.mensaje.error"), recurso.getString("registro.mensaje.error.detalle"), "fRegistros:growl");
        }
    }
    
    public void cerrar(){
        loginAccessBean.logout();
    }
    
    public String getIdentificadorSeleccionado() {
        return identificadorSeleccionado;
    }

    public void setIdentificadorSeleccionado(String identificadorSeleccionado) {
        this.identificadorSeleccionado = identificadorSeleccionado;
    }

    public Boolean getRegistro() {
        return registro;
    }

    public void setRegistro(Boolean registro) {
        this.registro = registro;
    }

    public Usuario getUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(Usuario usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public void setLoginAccessBean(LoginAccessBean loginAccessBean) {
        this.loginAccessBean = loginAccessBean;
    }

}

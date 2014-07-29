/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service;


import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface AdministracionService {

    public List<Perfil> getPerfiles() throws ServicesException;

    public void guardarPerfil(Perfil perfil) throws ServicesException;

    public void eliminarPerfil(Perfil perfil) throws ServicesException;

    public void actualizarPerfil(Perfil perfil) throws ServicesException;

    public List<Usuario> getUsuariosxPerfil(Perfil perfil) throws ServicesException;

    public List<Usuario> getUsuarios();

    public void guardarUsuario(Usuario usuario) throws ServicesException;

    public void actualizarUsuario(Usuario usuario) throws ServicesException;

    public void eliminarUsuario(Usuario usuario) throws ServicesException;

    public void resetClaveUsuario(Usuario usuario, String clave) throws ServicesException;

    public List<Empresa> getEmpresas() throws ServicesException;

    public void guardarEmpresa(Empresa empresa) throws ServicesException;

    public void actualizarEmpresa(Empresa empresa) throws ServicesException;

    public void eliminarEmpresa(Empresa empresa) throws ServicesException;
   
    public List<Usuario> getUsuariosByEmpresas(Empresa empresa) throws ServicesException;
    
    public List<Menu> getMenusPadre() throws ServicesException;
    
    public List<Menu> getMenusHijos(Menu padre) throws ServicesException;
    
    public Catalogo getCatalogo(Long numero) throws ServicesException;
    
      
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service;


import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.exception.ServicesException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface SeguridadService {

    public boolean IsValidUser(String nick, String password) throws ServicesException;

    public Usuario getUsuarioByNick(String nick) throws ServicesException;

    public List<Menu> getMenuByPerfil(Menu papa, Perfil perfil) throws ServicesException;
    
    public boolean buscarMenuxPerfil(Menu menu,Perfil perfil) throws ServicesException;
    
    public void asignarPermiso(Menu menu,Perfil perfil) throws ServicesException;
    
    public void eliminarPermiso(Menu menu,Perfil perfil) throws ServicesException;
}

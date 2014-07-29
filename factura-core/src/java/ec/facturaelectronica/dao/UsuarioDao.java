/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;


import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface UsuarioDao extends GenericDao<Usuario, Long> {

    public Usuario getUsuario(String username, String password);

    public Usuario getUsuarioByNick(String username);
    
    public List<Usuario> getUsuariosxPerfil(Perfil perfil);
    
    public List<Usuario> getUsuarios();
    
    public List<Usuario> getUsuarioByEmpresa(Empresa empresa);
    
}

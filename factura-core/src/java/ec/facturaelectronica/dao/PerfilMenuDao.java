/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;


import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.PerfilMenu;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface PerfilMenuDao  extends GenericDao<PerfilMenu , Long>{
    public PerfilMenu getPerfilMenus(Menu menu, Perfil perfil);
}

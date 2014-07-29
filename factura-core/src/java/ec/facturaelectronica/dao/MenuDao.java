/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;


import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface MenuDao extends GenericDao<Menu , Long>{
      public List<Menu> getMenuByPerfil(Menu padre, Perfil perfil);
      
      public List<Menu> getMenusPadres();
      
      public List<Menu> getMenusHijas(Menu padre);

}

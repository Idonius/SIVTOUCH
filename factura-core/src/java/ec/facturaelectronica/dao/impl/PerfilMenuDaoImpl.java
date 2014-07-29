/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;


import ec.facturaelectronica.dao.PerfilMenuDao;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.PerfilMenu;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class PerfilMenuDaoImpl  extends GenericDaoImpl<PerfilMenu, Long> implements PerfilMenuDao {

    public PerfilMenuDaoImpl() {
        super(PerfilMenu.class);
    }

    @Override
    public List<PerfilMenu> getPerfilMenus(Menu menu, Perfil perfil) {
        List<PerfilMenu> perfilMenus;

        Query q = em.createNamedQuery("PerfilMenu.findMenuAndPermiso");
        q.setParameter("idMenu", menu);
        q.setParameter("idPerfil", perfil);
        

        perfilMenus = q.getResultList();

        return perfilMenus;

    }
    
}

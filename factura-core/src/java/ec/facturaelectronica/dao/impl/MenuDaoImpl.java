/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;


import ec.facturaelectronica.dao.MenuDao;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class MenuDaoImpl extends GenericDaoImpl<Menu, Long> implements MenuDao {

    public MenuDaoImpl() {
        super(Menu.class);
    }

    @Override
    public List<Menu> getMenuByPerfil(Menu padre, Perfil perfil) {
        List<Menu> modulos;
        Query q;
        if (padre == null) {
            q = em.createNamedQuery("Menu.findMenusByPerfil");
            q.setParameter("perfil", perfil);
        } else {
            q = em.createQuery("SELECT m FROM PerfilMenu mp INNER JOIN mp.idMenu m WHERE mp.idPerfil = :perfil and m.menIdMenu = :papa  order by m.ordenMenu");
            q.setParameter("perfil", perfil);
            q.setParameter("papa", padre);


        }

        modulos = q.getResultList();



        return modulos;
    }

    @Override
    public List<Menu> getMenusPadres() {
        List<Menu> modulos;
        Query q;
        q = em.createNamedQuery("Menu.findMenus");

        modulos = q.getResultList();

        return modulos;

    }

    @Override
    public List<Menu> getMenusHijas(Menu padre) {

        List<Menu> modulos;
        Query q;
        q = em.createNamedQuery("Menu.findHijos");
        q.setParameter("menIdMenu", padre);

        modulos = q.getResultList();

        return modulos;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.MenuDao;
import ec.facturaelectronica.dao.PerfilMenuDao;
import ec.facturaelectronica.dao.UsuarioDao;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.PerfilMenu;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.service.SeguridadService;
import ec.facturaelectronica.service.util.Util;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class SeguridadServiceImpl implements SeguridadService  {

    @EJB
    UsuarioDao usuarioDao;
    @EJB
    MenuDao menuDao;
    @EJB
    PerfilMenuDao perfilMenuDao;

    @Override
    public boolean IsValidUser(String nick, String password) throws ServicesException {
        try {
            String pass = Util.md5(password);
            Usuario usuario = usuarioDao.getUsuario(nick, pass);
            boolean val = false;

            if (usuario == null) {
                val = false;
            } else {
                val = true;
            }
            return val;
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @Override
    public Usuario getUsuarioByNick(String nick) throws ServicesException {

        return usuarioDao.getUsuarioByNick(nick);

    }

    @Override
    public List<Menu> getMenuByPerfil(Menu papa, Perfil perfil) throws ServicesException {
        return menuDao.getMenuByPerfil(papa, perfil);

    }

    @Override
    public boolean buscarMenuxPerfil(Menu menu, Perfil perfil) throws ServicesException {
        boolean r = false;

        List<PerfilMenu> lista = perfilMenuDao.getPerfilMenus(menu, perfil);

        if (lista.isEmpty()) {
            r = false;
        } else {
            r = true;
        }

        return r;
    }

    @Override
    public void asignarPermiso(Menu menu, Perfil perfil) throws ServicesException {
        PerfilMenu pm;
        try {
            pm = new PerfilMenu();
            pm.setIdMenu(menu);
            pm.setIdPerfil(perfil);

            perfilMenuDao.insert(pm);

            //buscar padre
            buscarPadre(menu, perfil);

        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }

    }

    private void buscarPadre(Menu men, Perfil perfil) throws ServicesException {

        Menu padre = men.getMenIdMenu();

        if (padre != null) {
            if (!buscarMenuxPerfil(padre, perfil)) {
                PerfilMenu pm = new PerfilMenu();
                pm.setIdMenu(padre);
                pm.setIdPerfil(perfil);
                perfilMenuDao.insert(pm);
                buscarPadre(padre, perfil);
            }
        }

    }

    @Override
    public void eliminarPermiso(Menu menu, Perfil perfil) throws ServicesException {
        try {
            List<PerfilMenu> lista = perfilMenuDao.getPerfilMenus(menu, perfil);

            if (!lista.isEmpty()) {
                PerfilMenu pm = lista.get(0);
                PerfilMenu perfilMenu = null;
                perfilMenu = perfilMenuDao.load(pm.getIdPerfilMenu());
                perfilMenuDao.delete(pm);
            }

        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }



    }
}

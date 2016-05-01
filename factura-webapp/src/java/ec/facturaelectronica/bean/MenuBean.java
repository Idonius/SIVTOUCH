/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.SeguridadService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author desarrollotic
 */
@ManagedBean
@SessionScoped
public class MenuBean extends RecursosServices implements Serializable {

    private String menuDym;
    private StringBuilder str;
    private StringBuilder strSubMenu;
    private String subMenuDym;
    private String username;

    @EJB
    SeguridadService seguridadService;

    public MenuBean() {
    }

    @PostConstruct
    public void initMenuBean() {
        System.out.println("Entra");
        armaMenu();
    }

    public void armaMenu() {
        List<Menu> menu;
        Integer item = null;
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        try {

            Usuario usuario = seguridadService.getUsuarioByNick(principal.getName());
            username=usuario.getNombreUsuario();
            menu = seguridadService.getMenuByPerfil(null, usuario.getIdPerfil());
            str = new StringBuilder();
            strSubMenu = new StringBuilder();

            for (Menu m : menu) {
                String layout = "layout" + m.getIdMenu().toString();
                if (item == null) {
                    str.append("<li><a href=\"#\" class='active'  rel=\"").append(layout).append("\"> <i class='").append(m.getImagenMenu()).append("'></i> <span class=\"layout-tab-menu-link-text\"><span></span>").append(m.getNombreMenu()).append("</span> </a></li>");

                } else {
                    str.append("<li><a href=\"#\"  rel=\"").append(layout).append("\"> <i class='").append(m.getImagenMenu()).append("'></i> <span class=\"layout-tab-menu-link-text\"><span></span>").append(m.getNombreMenu()).append("</span> </a></li>");

                }
                item = 1;
                armarSubMenus(usuario, m);
            }
            menuDym = str.toString();
            subMenuDym = strSubMenu.toString();

        } catch (ServicesException ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    public void armarSubMenus(Usuario usuario, Menu menuPadre) {
        try {
            List<Menu> menu;
            String layout = "layout" + menuPadre.getIdMenu().toString();
            menu = seguridadService.getMenuByPerfil(menuPadre, usuario.getIdPerfil());
            strSubMenu.append("<ul class=\"layout-tab-submenu\" id=\"").append(layout).append("\">");
            strSubMenu.append(" <li>");
            strSubMenu.append(" <a class=\"TexAlCenter\">").append(menuPadre.getNombreMenu()).append("</a>");
            strSubMenu.append("<ul>");
            for (Menu m : menu) {
                strSubMenu.append(" <div class=\"EmptyBox20\"></div>");
                strSubMenu.append("<li><a href=\"").append(m.getUrlMenu()).append("?layout=").append(layout).append("\" class=\"menulink TexAlCenter\"> <i class=\"").append(m.getImagenMenu()).append(" ").append(m.getColor()).append(" Fs30 Wid100 Fnone\"></i>").append(m.getNombreMenu()).append(" </a></li>");
            }
            strSubMenu.append("</ul>");
            strSubMenu.append("</li>");
            strSubMenu.append("</ul>");

        } catch (ServicesException ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the subMenuDym
     */
    public String getSubMenuDym() {
        return subMenuDym;
    }

    /**
     * @param subMenuDym the subMenuDym to set
     */
    public void setSubMenuDym(String subMenuDym) {
        this.subMenuDym = subMenuDym;
    }

    public String getMenuDym() {
        return menuDym;
    }

    public void setMenuDym(String menuDym) {
        this.menuDym = menuDym;
    }

    public StringBuilder getStr() {
        return str;
    }

    public void setStr(StringBuilder str) {
        this.str = str;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

}

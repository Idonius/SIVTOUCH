/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;


import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.service.SeguridadService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author desarrollotic
 */
@ManagedBean
@ViewScoped
public class MenuBean extends RecursosServices  implements Serializable {
    
    @ManagedProperty("#{loginAccessBean}")
    private LoginAccessBean login;
    private String menuDym;
    private StringBuilder str;
    @EJB
    SeguridadService seguridadService;

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

    public LoginAccessBean getLogin() {
        return login;
    }

    public void setLogin(LoginAccessBean login) {
        this.login = login;
    }

    public MenuBean() {
    }

    @PostConstruct
    public void initMenuBean() {
        String ruta = recurso.getString("login.urllogin");

        try {
            if (login.getUsuarioLogin() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(ruta);
                
                
            }
            armaMenu();

        } catch (IOException ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void armaMenu() {
        List<Menu> menu;
        try {
            menu = seguridadService.getMenuByPerfil(null, login.getUsuarioLogin().getIdPerfil());

            str = new StringBuilder();
            str.append("<ul>");

            for (Menu m : menu) {
                if (m.getUrlMenu() == null) {
                    str.append("<li><a href='#'><i class='").append(m.getImagenMenu()).append("'></i><span class='menu-item-parent'>").append(m.getNombreMenu()).append("</span></a>");
                } else {
                    str.append("<li><a href='").append(m.getUrlMenu()).append("'><i class='").append(m.getImagenMenu()).append("'></i><span class='menu-item-parent'>").append(m.getNombreMenu()).append("</span></a>");
                }


                armaMenuHtml(m);


            }

            str.append("</ul>");
            menuDym = str.toString();


        } catch (ServicesException ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void armaMenuHtml(Menu m) {
        List<Menu> menu;
        try {
            menu = seguridadService.getMenuByPerfil(m, login.getUsuarioLogin().getIdPerfil());

            if (menu.size() > 0) {
                str.append("<ul>");
                for (Menu mr : menu) {

                    if (mr.getUrlMenu() == null) {
                        str.append("<li><a href='#'><i class='").append(mr.getImagenMenu()).append("'></i><span class='menu-item-parent'>").append(mr.getNombreMenu()).append("</span></a>");
                    } else {
                        str.append("<li><a href='").append(mr.getUrlMenu()).append("'><i class='").append(mr.getImagenMenu()).append("'></i><span class='menu-item-parent'>").append(mr.getNombreMenu()).append("</span></a>");
                    }

                    armaMenuHtml(mr);
                }
                str.append("</ul>");
            }


            str.append("</li>");


        } catch (ServicesException ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }





    }

    public String home() {
        return "index.xhtml";
    }

    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();

        ExternalContext externalContext = context.getExternalContext();

        Object session = externalContext.getSession(false);

        HttpSession httpSession = (HttpSession) session;

        httpSession.invalidate();

        String ruta = recurso.getString("login.main");


        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(ruta);

        } catch (IOException ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }


        return "";

    }
}

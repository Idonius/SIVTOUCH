/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Christian
 */
@Entity
@Table(name = "menu")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Menu.findMenusByPerfil", query = "SELECT m FROM PerfilMenu mp INNER JOIN mp.idMenu m WHERE  mp.idPerfil = :perfil and m.menIdMenu is null and m.estadoMenu='A' order by m.ordenMenu"),
    @NamedQuery(name = "Menu.findMenus", query = "SELECT m FROM Menu m Where m.estadoMenu='A' and m.menIdMenu is null order by m.ordenMenu"),
    @NamedQuery(name = "Menu.findHijos", query = "SELECT m FROM Menu m Where m.estadoMenu='A' and m.menIdMenu= :menIdMenu order by m.ordenMenu"),
    @NamedQuery(name = "Menu.findByImagenMenu", query = "SELECT m FROM Menu m WHERE m.imagenMenu = :imagenMenu")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_menu")
    private Integer idMenu;
    @Size(max = 100)
    @Column(name = "nombre_menu")
    private String nombreMenu;
    @Size(max = 100)
    @Column(name = "url_menu")
    private String urlMenu;
    @Size(max = 20)
    @Column(name = "color_menu")
    private String color;
    @Size(max = 1)
    @Column(name = "estado_menu")
    private String estadoMenu;
    @Size(max = 100)
    @Column(name = "imagen_menu")
    private String imagenMenu;
    @Size(max = 3)
    @Column(name = "orden_menu")
    private String ordenMenu;
    @OneToMany(mappedBy = "menIdMenu")
    private List<Menu> menuList;
    @JoinColumn(name = "men_id_menu", referencedColumnName = "id_menu")
    @ManyToOne
    private Menu menIdMenu;
    @OneToMany(mappedBy = "idMenu")
    private List<PerfilMenu> perfilMenuList;

    public Menu() {
    }

    public Menu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public String getUrlMenu() {
        return urlMenu;
    }

    public void setUrlMenu(String urlMenu) {
        this.urlMenu = urlMenu;
    }

    public String getEstadoMenu() {
        return estadoMenu;
    }

    public void setEstadoMenu(String estadoMenu) {
        this.estadoMenu = estadoMenu;
    }

    public String getImagenMenu() {
        return imagenMenu;
    }

    public void setImagenMenu(String imagenMenu) {
        this.imagenMenu = imagenMenu;
    }

    public String getOrdenMenu() {
        return ordenMenu;
    }

    public void setOrdenMenu(String ordenMenu) {
        this.ordenMenu = ordenMenu;
    }

    @XmlTransient
    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public Menu getMenIdMenu() {
        return menIdMenu;
    }

    public void setMenIdMenu(Menu menIdMenu) {
        this.menIdMenu = menIdMenu;
    }

    @XmlTransient
    public List<PerfilMenu> getPerfilMenuList() {
        return perfilMenuList;
    }

    public void setPerfilMenuList(List<PerfilMenu> perfilMenuList) {
        this.perfilMenuList = perfilMenuList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMenu != null ? idMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.idMenu == null && other.idMenu != null) || (this.idMenu != null && !this.idMenu.equals(other.idMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Menu[ idMenu=" + idMenu + " ]";
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

  

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;

/**
 *
 * @author Christian
 */
@Entity
@Table(name = "perfil_menu")
@SequenceGenerator(sequenceName = "seq_perfil_menu", name = "seq_perfil_menu_gen", allocationSize = 1)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PerfilMenu.findAll", query = "SELECT p FROM PerfilMenu p"),
    @NamedQuery(name = "PerfilMenu.findMenuAndPermiso", query = "SELECT p FROM PerfilMenu p WHERE p.idMenu=:idMenu and p.idPerfil=:idPerfil")})

public class PerfilMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "seq_perfil_menu_gen")
    @Column(name = "id_perfil_menu")
    private Long idPerfilMenu;
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @ManyToOne
    private Perfil idPerfil;
    @JoinColumn(name = "id_menu", referencedColumnName = "id_menu")
    @ManyToOne
    private Menu idMenu;

    public PerfilMenu() {
    }

    public PerfilMenu(Long idPerfilMenu) {
        this.idPerfilMenu = idPerfilMenu;
    }

    public Long getIdPerfilMenu() {
        return idPerfilMenu;
    }

    public void setIdPerfilMenu(Long idPerfilMenu) {
        this.idPerfilMenu = idPerfilMenu;
    }

    public Perfil getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Perfil idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Menu getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Menu idMenu) {
        this.idMenu = idMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfilMenu != null ? idPerfilMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilMenu)) {
            return false;
        }
        PerfilMenu other = (PerfilMenu) object;
        if ((this.idPerfilMenu == null && other.idPerfilMenu != null) || (this.idPerfilMenu != null && !this.idPerfilMenu.equals(other.idPerfilMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.PerfilMenu[ idPerfilMenu=" + idPerfilMenu + " ]";
    }

}

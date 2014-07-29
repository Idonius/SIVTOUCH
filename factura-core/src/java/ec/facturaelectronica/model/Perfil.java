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
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
/**
 *
 * @author Christian
 */
@Entity
@Table(name = "perfil")
@SequenceGenerator(sequenceName = "seq_perfil", name = "seq_perfil_gen", allocationSize = 1)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perfil.find", query = "SELECT p FROM Perfil p where p.idEstadoCatalogo.idCatalogo = :idCatalogo order by p.nombrePerfil ASC"),
    @NamedQuery(name = "Perfil.findByNombrePerfil", query = "SELECT p FROM Perfil p WHERE p.nombrePerfil = :nombrePerfil order by p.nombrePerfil ASC")})
public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "seq_perfil_gen")
    @Column(name = "id_perfil")
    private Integer idPerfil;
    @Size(max = 50)
    @Column(name = "nombre_perfil")
    private String nombrePerfil;
    @OneToMany(mappedBy = "idPerfil")
    private List<Usuario> usuarioList;
    @JoinColumn(name = "id_estado_catalogo", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo idEstadoCatalogo;
    @OneToMany(mappedBy = "idPerfil")
    private List<PerfilMenu> perfilMenuList;

    public Perfil() {
    }

    public Perfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Catalogo getIdEstadoCatalogo() {
        return idEstadoCatalogo;
    }

    public void setIdEstadoCatalogo(Catalogo idEstadoCatalogo) {
        this.idEstadoCatalogo = idEstadoCatalogo;
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
        hash += (idPerfil != null ? idPerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.idPerfil == null && other.idPerfil != null) || (this.idPerfil != null && !this.idPerfil.equals(other.idPerfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Perfil[ idPerfil=" + idPerfil + " ]";
    }
    
}

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Christian
 */
@Entity
@Table(name = "grupo_catalogo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoCatalogo.findAll", query = "SELECT g FROM GrupoCatalogo g"),
    @NamedQuery(name = "GrupoCatalogo.findByIdGrpCatalogo", query = "SELECT g FROM GrupoCatalogo g WHERE g.idGrpCatalogo = :idGrpCatalogo"),
    @NamedQuery(name = "GrupoCatalogo.findByDescGrpCatalogo", query = "SELECT g FROM GrupoCatalogo g WHERE g.descGrpCatalogo = :descGrpCatalogo"),
    @NamedQuery(name = "GrupoCatalogo.findByNombreGrpCatalogo", query = "SELECT g FROM GrupoCatalogo g WHERE g.nombreGrpCatalogo = :nombreGrpCatalogo")})
public class GrupoCatalogo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_grp_catalogo")
    private Long idGrpCatalogo;
    @Size(max = 100)
    @Column(name = "desc_grp_catalogo")
    private String descGrpCatalogo;
    @Size(max = 50)
    @Column(name = "nombre_grp_catalogo")
    private String nombreGrpCatalogo;
    @OneToMany(mappedBy = "grupoCatalogo")
    private List<Catalogo> catalogos;

    public GrupoCatalogo() {
    }

    public GrupoCatalogo(Long idGrpCatalogo) {
        this.idGrpCatalogo = idGrpCatalogo;
    }

    public Long getIdGrpCatalogo() {
        return idGrpCatalogo;
    }

    public void setIdGrpCatalogo(Long idGrpCatalogo) {
        this.idGrpCatalogo = idGrpCatalogo;
    }

    public String getDescGrpCatalogo() {
        return descGrpCatalogo;
    }

    public void setDescGrpCatalogo(String descGrpCatalogo) {
        this.descGrpCatalogo = descGrpCatalogo;
    }

    public String getNombreGrpCatalogo() {
        return nombreGrpCatalogo;
    }

    public void setNombreGrpCatalogo(String nombreGrpCatalogo) {
        this.nombreGrpCatalogo = nombreGrpCatalogo;
    }

    public List<Catalogo> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrpCatalogo != null ? idGrpCatalogo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoCatalogo)) {
            return false;
        }
        GrupoCatalogo other = (GrupoCatalogo) object;
        if ((this.idGrpCatalogo == null && other.idGrpCatalogo != null) || (this.idGrpCatalogo != null && !this.idGrpCatalogo.equals(other.idGrpCatalogo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.GrupoCatalogo[ idGrpCatalogo=" + idGrpCatalogo + " ]";
    }
    
}

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
@Table(name = "catalogo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Catalogo.findAll", query = "SELECT c FROM Catalogo c"),
    @NamedQuery(name = "Catalogo.findAllByTrans", query="SELECT c FROM Catalogo c WHERE c.grupoCatalogo.idGrpCatalogo=3"),
    @NamedQuery(name = "Catalogo.findAllByGrupo", query="SELECT c FROM Catalogo c WHERE c.grupoCatalogo=:grupo"),
    @NamedQuery(name = "Catalogo.findByIdCatalogo", query = "SELECT c FROM Catalogo c WHERE c.idCatalogo = :idCatalogo"),
    @NamedQuery(name = "Catalogo.findByNombreCatalogo", query = "SELECT c FROM Catalogo c WHERE c.nombreCatalogo = :nombreCatalogo"),
    @NamedQuery(name = "Catalogo.findByAbrCatalogo", query = "SELECT c FROM Catalogo c WHERE c.abrCatalogo = :abrCatalogo"),
    @NamedQuery(name = "Catalogo.findByIdGrpCatalogo", query = "SELECT c FROM Catalogo c WHERE c.grupoCatalogo.idGrpCatalogo = :idGrpCatalogo"),
    @NamedQuery(name = "Catalogo.findByGrpCatalogoByNombreCatalogo", query = "SELECT c FROM Catalogo c WHERE c.grupoCatalogo = :grpCatalogo and c.nombreCatalogo=:nombre")
})
public class Catalogo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_catalogo")
    private Long idCatalogo;
    
    @Size(max = 50)
    @Column(name = "nombre_catalogo")
    private String nombreCatalogo;
    
    @Size(max = 10)
    @Column(name = "abr_catalogo")
    private String abrCatalogo;
    
      
    @OneToMany(mappedBy = "idEstadoCatalogo")
    private List<Usuario> usuarioList;
    
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa idEmpresa;
    
    @OneToMany(mappedBy = "idEstadoCatalogo")
    private List<Empresa> empresaList;
    
    @OneToMany(mappedBy = "idEstadoCatalogo")
    private List<Perfil> perfilList;
    
    @JoinColumn(name = "id_grp_catalogo", referencedColumnName = "id_grp_catalogo")
    @ManyToOne
    private GrupoCatalogo grupoCatalogo;

    public Catalogo() {
    }

    public Catalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public Long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getNombreCatalogo() {
        return nombreCatalogo;
    }

    public void setNombreCatalogo(String nombreCatalogo) {
        this.nombreCatalogo = nombreCatalogo;
    }

    public String getAbrCatalogo() {
        return abrCatalogo;
    }

    public void setAbrCatalogo(String abrCatalogo) {
        this.abrCatalogo = abrCatalogo;
    }

   

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @XmlTransient
    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    @XmlTransient
    public List<Perfil> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<Perfil> perfilList) {
        this.perfilList = perfilList;
    }

    public GrupoCatalogo getGrupoCatalogo() {
        return grupoCatalogo;
    }

    public void setGrupoCatalogo(GrupoCatalogo grupoCatalogo) {
        this.grupoCatalogo = grupoCatalogo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCatalogo != null ? idCatalogo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Catalogo)) {
            return false;
        }
        Catalogo other = (Catalogo) object;
        if ((this.idCatalogo == null && other.idCatalogo != null) || (this.idCatalogo != null && !this.idCatalogo.equals(other.idCatalogo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Catalogo[ idCatalogo=" + idCatalogo + " ]";
    }
    
}

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
@Table(name = "empresa")
@SequenceGenerator(sequenceName = "seq_empresa", name = "seq_empresa_gen", allocationSize = 1)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e WHERE e.idEstadoCatalogo.idCatalogo =:idEstadoCatalogo"),
    @NamedQuery(name = "Empresa.findByEmpresa", query = "SELECT e FROM Empresa e WHERE e.idEstadoCatalogo.idCatalogo =:idEstadoCatalogo and e.rucEmpresa=:rucEmpresa"),
    @NamedQuery(name = "Empresa.findAllByPlan", query = "SELECT e FROM Empresa e WHERE e.plan =:plan AND e.idEstadoCatalogo.nombreCatalogo='Activo'")
    })
public class Empresa implements Serializable {
    @OneToMany(mappedBy = "idEmpresa")
    private List<Comprobante> comprobanteList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "seq_empresa_gen")
    @Column(name = "id_empresa")
    private Integer idEmpresa;
    @Size(max = 100)
    @Column(name = "nombre_empresa")
    private String nombreEmpresa;
    @Size(max = 200)
    @Column(name = "direccion_empresa")
    private String direccionEmpresa;
    @Size(max = 15)
    @Column(name = "telefono_empresa")
    private String telefonoEmpresa;
    @Size(max = 13)
    @Column(name = "ruc_empresa")
    private String rucEmpresa;
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
    @ManyToOne
    private Plan plan;
    @OneToMany(mappedBy = "idEmpresa")
    private List<Usuario> usuarioList;
    @OneToMany(mappedBy = "idEmpresa")
    private List<Catalogo> catalogoList;
    @JoinColumn(name = "id_estado_catalogo", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo idEstadoCatalogo;

    public Empresa() {
    }

    public Empresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getTelefonoEmpresa() {
        return telefonoEmpresa;
    }

    public void setTelefonoEmpresa(String telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<Catalogo> getCatalogoList() {
        return catalogoList;
    }

    public void setCatalogoList(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

    public Catalogo getIdEstadoCatalogo() {
        return idEstadoCatalogo;
    }

    public void setIdEstadoCatalogo(Catalogo idEstadoCatalogo) {
        this.idEstadoCatalogo = idEstadoCatalogo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Empresa[ idEmpresa=" + idEmpresa + " ]";
    }

    @XmlTransient
    public List<Comprobante> getComprobanteList() {
        return comprobanteList;
    }

    public void setComprobanteList(List<Comprobante> comprobanteList) {
        this.comprobanteList = comprobanteList;
    }
    
}

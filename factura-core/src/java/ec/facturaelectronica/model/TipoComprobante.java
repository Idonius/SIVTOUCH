/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "tipo_comprobante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "tipoComprobante.findAll", query = "SELECT tc FROM TipoComprobante tc"),
    @NamedQuery(name = "tipoComprobante.findByCodigo", query = "SELECT tc FROM TipoComprobante tc WHERE tc.codigoTipo=:codigoTipo"),
})
public class TipoComprobante implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_tipo_comprobante_gen", sequenceName = "seq_tipo_comprobante", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_tipo_comprobante_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_tipo_comprobante")
    private Integer id;

    @Column(name = "nombre_tipo_comprobante", length = 20)
    private String nombreTipoComprobante;

    @Column(name = "alias_tipo_comprobante", length = 2)
    private String alias;

    @Column(name = "codigo_sri_tipo_comprobante", length = 5)
    private String codigoTipo;

    @OneToMany(mappedBy = "idTipoComprobante")
    private List<Comprobante> comprobantes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreTipoComprobante() {
        return nombreTipoComprobante;
    }

    public void setNombreTipoComprobante(String nombreTipoComprobante) {
        this.nombreTipoComprobante = nombreTipoComprobante;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Comprobante> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<Comprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoComprobante)) {
            return false;
        }
        TipoComprobante other = (TipoComprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.TipoComprobante[ id=" + id + " ]";
    }

    /**
     * @return the codigoTipo
     */
    public String getCodigoTipo() {
        return codigoTipo;
    }

    /**
     * @param codigoTipo the codigoTipo to set
     */
    public void setCodigoTipo(String codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

}

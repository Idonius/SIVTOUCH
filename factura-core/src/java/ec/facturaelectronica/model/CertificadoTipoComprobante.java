/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "certificado_tipo_comprobante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CertificadoTipoComprobante.findAll", query="SELECT ctc FROM CertificadoTipoComprobante ctc WHERE ctc.catalogo.nombreCatalogo='Activo'")
})
public class CertificadoTipoComprobante implements Serializable{
    @Id
    @SequenceGenerator(name = "seq_certificadoTipoComprobante_gen", sequenceName = "seq_certificado_tipo_comprobante", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_certificadoTipoComprobante_gen")
    @Column(name = "id_certificado_tipo_comprobante")
    private Integer id;
    
    @JoinColumn(name = "id_certificado", referencedColumnName = "id_certificado")
    @ManyToOne
    private Certificado certificado;
    
    @JoinColumn(name = "id_tipo_comprobante", referencedColumnName = "id_tipo_comprobante")
    @ManyToOne
    private TipoComprobante tipoComprobante;
    
    @JoinColumn(name = "id_estado", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo catalogo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
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
        if (!(object instanceof CertificadoTipoComprobante)) {
            return false;
        }
        CertificadoTipoComprobante other = (CertificadoTipoComprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.CertificadoTipoComprobante[ id=" + id + " ]";
    }
    
}

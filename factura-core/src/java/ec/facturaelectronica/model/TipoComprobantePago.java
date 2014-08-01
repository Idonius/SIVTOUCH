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
import javax.persistence.GenerationType;
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
@Table(name = "tipo_comprobante_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoComprobantePago.findAll", query = "SELECT t FROM TipoComprobantePago t"),
    @NamedQuery(name = "TipoComprobantePago.findByIdTipoComprobantePago", query = "SELECT t FROM TipoComprobantePago t WHERE t.idTipoComprobantePago = :idTipoComprobantePago"),
    @NamedQuery(name = "TipoComprobantePago.findByNumeroComprobantesTipo", query = "SELECT t FROM TipoComprobantePago t WHERE t.numeroComprobantesTipo = :numeroComprobantesTipo")})
public class TipoComprobantePago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_tipo_comprobante_pago_gen", sequenceName = "seq_tipo_comprobante_pago", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_tipo_comprobante_pago_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_tipo_comprobante_pago")
    private Integer idTipoComprobantePago;
    @Column(name = "numero_comprobantes_tipo")
    private Integer numeroComprobantesTipo;
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    @ManyToOne
    private Pago idPago;
    @JoinColumn(name = "id_tipo_comprobante", referencedColumnName = "id_tipo_comprobante")
    @ManyToOne
    private TipoComprobante tipoComprobante;

    public TipoComprobantePago() {
    }

    public TipoComprobantePago(Integer idTipoComprobantePago) {
        this.idTipoComprobantePago = idTipoComprobantePago;
    }

    public Integer getIdTipoComprobantePago() {
        return idTipoComprobantePago;
    }

    public void setIdTipoComprobantePago(Integer idTipoComprobantePago) {
        this.idTipoComprobantePago = idTipoComprobantePago;
    }

    public Integer getNumeroComprobantesTipo() {
        return numeroComprobantesTipo;
    }

    public void setNumeroComprobantesTipo(Integer numeroComprobantesTipo) {
        this.numeroComprobantesTipo = numeroComprobantesTipo;
    }

    public Pago getIdPago() {
        return idPago;
    }

    public void setIdPago(Pago idPago) {
        this.idPago = idPago;
    }

    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoComprobantePago != null ? idTipoComprobantePago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoComprobantePago)) {
            return false;
        }
        TipoComprobantePago other = (TipoComprobantePago) object;
        if ((this.idTipoComprobantePago == null && other.idTipoComprobantePago != null) || (this.idTipoComprobantePago != null && !this.idTipoComprobantePago.equals(other.idTipoComprobantePago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.TipoComprobantePago[ idTipoComprobantePago=" + idTipoComprobantePago + " ]";
    }
    
}

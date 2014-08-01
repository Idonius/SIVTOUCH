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
@Table(name = "detalle_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallePago.findAll", query = "SELECT d FROM DetallePago d"),
    @NamedQuery(name = "DetallePago.findByIdDetallePago", query = "SELECT d FROM DetallePago d WHERE d.idDetallePago = :idDetallePago")})
public class DetallePago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_detalle_pago_gen", sequenceName = "seq_detalle_pago", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_detalle_pago_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_detalle_pago")
    private Integer idDetallePago;
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    @ManyToOne
    private Pago idPago;
    @JoinColumn(name = "id_comprobante", referencedColumnName = "id_comprobante")
    @ManyToOne
    private Comprobante comprobante;

    public DetallePago() {
    }

    public DetallePago(Integer idDetallePago) {
        this.idDetallePago = idDetallePago;
    }

    public Integer getIdDetallePago() {
        return idDetallePago;
    }

    public void setIdDetallePago(Integer idDetallePago) {
        this.idDetallePago = idDetallePago;
    }

    public Pago getIdPago() {
        return idPago;
    }

    public void setIdPago(Pago idPago) {
        this.idPago = idPago;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetallePago != null ? idDetallePago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePago)) {
            return false;
        }
        DetallePago other = (DetallePago) object;
        if ((this.idDetallePago == null && other.idDetallePago != null) || (this.idDetallePago != null && !this.idDetallePago.equals(other.idDetallePago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.DetallePago[ idDetallePago=" + idDetallePago + " ]";
    }
    
}

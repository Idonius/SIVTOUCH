/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "cobro_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CobroPago.findAll", query = "SELECT c FROM CobroPago c"),
    @NamedQuery(name = "CobroPago.findByIdCobroPago", query = "SELECT c FROM CobroPago c WHERE c.idCobroPago = :idCobroPago"),
    @NamedQuery(name = "CobroPago.findByFechaTransaccionCobro", query = "SELECT c FROM CobroPago c WHERE c.fechaTransaccionCobro = :fechaTransaccionCobro"),
    @NamedQuery(name = "CobroPago.findByNumeroTransaccionCobro", query = "SELECT c FROM CobroPago c WHERE c.numeroTransaccionCobro = :numeroTransaccionCobro"),
    @NamedQuery(name = "CobroPago.findByFacturaCobro", query = "SELECT c FROM CobroPago c WHERE c.facturaCobro = :facturaCobro")
})
public class CobroPago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_cobro_pago_gen", sequenceName = "seq_cobro_pago", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_cobro_pago_gen")
    @Column(name = "id_cobro_pago")
    private Integer idCobroPago;
    
    @Column(name = "fecha_transaccion_cobro")
    @Temporal(TemporalType.DATE)
    private Date fechaTransaccionCobro;
    
    @Size(max = 30)
    @Column(name = "numero_transaccion_cobro")
    private String numeroTransaccionCobro;
    
    @Column(name = "factura_cobro")
    private Integer facturaCobro;
    
    @JoinColumn(name = "id_tipo_transaccion", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo tipoTransaccion;
    
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    @ManyToOne
    private Pago idPago;    

    public CobroPago() {
    }

    public CobroPago(Integer idCobroPago) {
        this.idCobroPago = idCobroPago;
    }

    public Integer getIdCobroPago() {
        return idCobroPago;
    }

    public void setIdCobroPago(Integer idCobroPago) {
        this.idCobroPago = idCobroPago;
    }

    public Date getFechaTransaccionCobro() {
        return fechaTransaccionCobro;
    }

    public void setFechaTransaccionCobro(Date fechaTransaccionCobro) {
        this.fechaTransaccionCobro = fechaTransaccionCobro;
    }

    public String getNumeroTransaccionCobro() {
        return numeroTransaccionCobro;
    }

    public void setNumeroTransaccionCobro(String numeroTransaccionCobro) {
        this.numeroTransaccionCobro = numeroTransaccionCobro;
    }

    public Integer getFacturaCobro() {
        return facturaCobro;
    }

    public void setFacturaCobro(Integer facturaCobro) {
        this.facturaCobro = facturaCobro;
    }

    public Catalogo getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(Catalogo tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public Pago getIdPago() {
        return idPago;
    }

    public void setIdPago(Pago idPago) {
        this.idPago = idPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCobroPago != null ? idCobroPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CobroPago)) {
            return false;
        }
        CobroPago other = (CobroPago) object;
        if ((this.idCobroPago == null && other.idCobroPago != null) || (this.idCobroPago != null && !this.idCobroPago.equals(other.idCobroPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.CobroPago[ idCobroPago=" + idCobroPago + " ]";
    }
    
}

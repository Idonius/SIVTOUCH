/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
    @NamedQuery(name = "Pago.findAllActive", query = "SELECT p FROM Pago p WHERE p.estado.nombreCatalogo='Activo'"),
    @NamedQuery(name = "Pago.findByIdPago", query = "SELECT p FROM Pago p WHERE p.idPago = :idPago"),
    @NamedQuery(name = "Pago.findByFechaGeneracionPago", query = "SELECT p FROM Pago p WHERE p.fechaGeneracionPago = :fechaGeneracionPago"),
    @NamedQuery(name = "Pago.findByFechaDesdePago", query = "SELECT p FROM Pago p WHERE p.fechaDesdePago = :fechaDesdePago"),
    @NamedQuery(name = "Pago.findByFechaHastaPago", query = "SELECT p FROM Pago p WHERE p.fechaHastaPago = :fechaHastaPago"),
    @NamedQuery(name = "Pago.findByFechaRegistroPago", query = "SELECT p FROM Pago p WHERE p.fechaRegistroPago = :fechaRegistroPago"),
    @NamedQuery(name = "Pago.findByMesPago", query = "SELECT p FROM Pago p WHERE p.mesPago = :mesPago"),
    @NamedQuery(name = "Pago.findByAnioPago", query = "SELECT p FROM Pago p WHERE p.anioPago = :anioPago"),
    @NamedQuery(name = "Pago.findByNumeroComprobantesPago", query = "SELECT p FROM Pago p WHERE p.numeroComprobantesPago = :numeroComprobantesPago"),
    @NamedQuery(name = "Pago.findByCostoPlanPago", query = "SELECT p FROM Pago p WHERE p.costoPlanPago = :costoPlanPago"),
    @NamedQuery(name = "Pago.findByCostoFactPlanPago", query = "SELECT p FROM Pago p WHERE p.costoFactPlanPago = :costoFactPlanPago"),
    @NamedQuery(name = "Pago.findByExcedenteComprobantesPago", query = "SELECT p FROM Pago p WHERE p.excedenteComprobantesPago = :excedenteComprobantesPago"),
    @NamedQuery(name = "Pago.findByTotalPago", query = "SELECT p FROM Pago p WHERE p.totalPago = :totalPago"),
    @NamedQuery(name = "Pago.findByFechaCancelaPago", query = "SELECT p FROM Pago p WHERE p.fechaCancelaPago = :fechaCancelaPago"),
    @NamedQuery(name = "Pago.findByFechaApruebaPago", query = "SELECT p FROM Pago p WHERE p.fechaApruebaPago = :fechaApruebaPago"),
    @NamedQuery(name = "Pago.findByObservacionesPago", query = "SELECT p FROM Pago p WHERE p.observacionesPago = :observacionesPago"),
    @NamedQuery(name = "Pago.findByEmpresaByMes", query = "SELECT p FROM Pago p WHERE p.empresa=:emp and p.mesPago=:mes")})
public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "seq_pago_gen", sequenceName = "seq_pago", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_pago_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_pago")
    private Integer idPago;
    @Column(name = "fecha_generacion_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaGeneracionPago;
    @Column(name = "fecha_desde_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdePago;
    @Column(name = "fecha_hasta_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaPago;
    @Column(name = "fecha_registro_pago")
    @Temporal(TemporalType.TIME)
    private Date fechaRegistroPago;
    @Column(name = "mes_pago")
    private Integer mesPago;
    @Column(name = "anio_pago")
    private Integer anioPago;
    @Column(name = "numero_comprobantes_pago")
    private Integer numeroComprobantesPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_plan_pago")
    private BigDecimal costoPlanPago;
    @Column(name = "costo_fact_plan_pago")
    private BigDecimal costoFactPlanPago;
    @Column(name = "excedente_comprobantes_pago")
    private Integer excedenteComprobantesPago;
    @Column(name = "total_pago")
    private BigDecimal totalPago;
    @Column(name = "fecha_cancela_pago")
    @Temporal(TemporalType.TIME)
    private Date fechaCancelaPago;
    @Column(name = "fecha_aprueba_pago")
    @Temporal(TemporalType.TIME)
    private Date fechaApruebaPago;
    @Size(max = 1000)
    @Column(name = "observaciones_pago")
    private String observacionesPago;
    @OneToMany(mappedBy = "idPago")
    private List<DetallePago> detallePagoList;
    @OneToMany(mappedBy = "idPago")
    private List<TipoComprobantePago> tipoComprobantePagoList;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa empresa;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo estado;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
    @ManyToOne
    private Plan plan;
    @JoinColumn(name = "id_usuario_cancela", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuarioCancelaPago;
    @JoinColumn(name = "id_usuario_aprueba_pago", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario usuarioApruebaPago;
    
    public Pago() {
    }

    public Pago(Integer idPago) {
        this.idPago = idPago;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Date getFechaGeneracionPago() {
        return fechaGeneracionPago;
    }

    public void setFechaGeneracionPago(Date fechaGeneracionPago) {
        this.fechaGeneracionPago = fechaGeneracionPago;
    }

    public Date getFechaDesdePago() {
        return fechaDesdePago;
    }

    public void setFechaDesdePago(Date fechaDesdePago) {
        this.fechaDesdePago = fechaDesdePago;
    }

    public Date getFechaHastaPago() {
        return fechaHastaPago;
    }

    public void setFechaHastaPago(Date fechaHastaPago) {
        this.fechaHastaPago = fechaHastaPago;
    }

    public Date getFechaRegistroPago() {
        return fechaRegistroPago;
    }

    public void setFechaRegistroPago(Date fechaRegistroPago) {
        this.fechaRegistroPago = fechaRegistroPago;
    }

    public Integer getMesPago() {
        return mesPago;
    }

    public void setMesPago(Integer mesPago) {
        this.mesPago = mesPago;
    }

    public Integer getAnioPago() {
        return anioPago;
    }

    public void setAnioPago(Integer anioPago) {
        this.anioPago = anioPago;
    }

    public Integer getNumeroComprobantesPago() {
        return numeroComprobantesPago;
    }

    public void setNumeroComprobantesPago(Integer numeroComprobantesPago) {
        this.numeroComprobantesPago = numeroComprobantesPago;
    }

    public BigDecimal getCostoPlanPago() {
        return costoPlanPago;
    }

    public void setCostoPlanPago(BigDecimal costoPlanPago) {
        this.costoPlanPago = costoPlanPago;
    }

    public BigDecimal getCostoFactPlanPago() {
        return costoFactPlanPago;
    }

    public void setCostoFactPlanPago(BigDecimal costoFactPlanPago) {
        this.costoFactPlanPago = costoFactPlanPago;
    }

    public Integer getExcedenteComprobantesPago() {
        return excedenteComprobantesPago;
    }

    public void setExcedenteComprobantesPago(Integer excedenteComprobantesPago) {
        this.excedenteComprobantesPago = excedenteComprobantesPago;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public Date getFechaCancelaPago() {
        return fechaCancelaPago;
    }

    public void setFechaCancelaPago(Date fechaCancelaPago) {
        this.fechaCancelaPago = fechaCancelaPago;
    }

    public Date getFechaApruebaPago() {
        return fechaApruebaPago;
    }

    public void setFechaApruebaPago(Date fechaApruebaPago) {
        this.fechaApruebaPago = fechaApruebaPago;
    }

    public String getObservacionesPago() {
        return observacionesPago;
    }

    public void setObservacionesPago(String observacionesPago) {
        this.observacionesPago = observacionesPago;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Catalogo getEstado() {
        return estado;
    }

    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Usuario getUsuarioCancelaPago() {
        return usuarioCancelaPago;
    }

    public void setUsuarioCancelaPago(Usuario usuarioCancelaPago) {
        this.usuarioCancelaPago = usuarioCancelaPago;
    }

    public Usuario getUsuarioApruebaPago() {
        return usuarioApruebaPago;
    }

    public void setUsuarioApruebaPago(Usuario usuarioApruebaPago) {
        this.usuarioApruebaPago = usuarioApruebaPago;
    }

    @XmlTransient
    public List<DetallePago> getDetallePagoList() {
        return detallePagoList;
    }

    public void setDetallePagoList(List<DetallePago> detallePagoList) {
        this.detallePagoList = detallePagoList;
    }

    @XmlTransient
    public List<TipoComprobantePago> getTipoComprobantePagoList() {
        return tipoComprobantePagoList;
    }

    public void setTipoComprobantePagoList(List<TipoComprobantePago> tipoComprobantePagoList) {
        this.tipoComprobantePagoList = tipoComprobantePagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPago != null ? idPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.idPago == null && other.idPago != null) || (this.idPago != null && !this.idPago.equals(other.idPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Pago[ idPago=" + idPago + " ]";
    }
    
}

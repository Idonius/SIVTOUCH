/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Christian
 */
@Entity
@SequenceGenerator(sequenceName = "seq_comprobante", name = "seq_comprobante_gen", allocationSize = 1)
@Table(name = "comprobante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comprobante.findByEmpresa", query = "SELECT c FROM Comprobante c WHERE c.idEmpresa=:idEmpresa and c.fechaComprobante>=:fechaDesde and c.fechaComprobante<=:fechaHasta order by c.fechaComprobante desc"),
    @NamedQuery(name = "Comprobante.findByArchivoComprobante", query = "SELECT c FROM Comprobante c WHERE c.archivoComprobante = :archivoComprobante")})
public class Comprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "seq_comprobante_gen")
    @Column(name = "id_comprobante")
    private Long idComprobante;
    @Size(max = 40)
    @Column(name = "estado_comprobante")
    private String estadoComprobante;
    @Size(max = 40)
    @Column(name = "ambiente_comprobante")
    private String ambienteComprobante;
    @Column(name = "identificador_comprobante")
    private Integer identificadorComprobante;
    @Size(max = 1000)
    @Column(name = "mensaje_comprobante")
    private String mensajeComprobante;
    @Size(max = 60)
    @Column(name = "tipo_mensaje_comprobante")
    private String tipoMensajeComprobante;
    @Size(max = 1000)
    @Column(name = "clave_comprobante")
    private String claveComprobante;
    @Size(max = 50)
    @Column(name = "secuencial_comprobante")
    private String secuencialComprobante;
    @Column(name = "fecha_comprobante")
    @Temporal(TemporalType.DATE)
    private Date fechaComprobante;
    @Column(name = "fecha_sistema_comprobante")
    @Temporal(TemporalType.DATE)
    private Date fechaSistemaComprobante;
    @Column(name = "total_comprobante")
    private BigDecimal totalComprobante;
    @Column(name = "numero_autorizacion_comprobante")
    private String numeroAutorizacionComprobante;
    @Column(name = "fecha_autorizacion_comprobante")
    @Temporal(TemporalType.DATE)
    private Date fechaAutorizacionComprobante;
    @Size(max = 100)
    @Column(name = "archivo_comprobante")
    private String archivoComprobante;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa idEmpresa;

    public Comprobante() {
    }

    public Comprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public Long getIdComprobante() {
        return idComprobante;
    }

    public String getNumeroAutorizacionComprobante() {
        return numeroAutorizacionComprobante;
    }

    public void setNumeroAutorizacionComprobante(String numeroAutorizacionComprobante) {
        this.numeroAutorizacionComprobante = numeroAutorizacionComprobante;
    }

    public Date getFechaAutorizacionComprobante() {
        return fechaAutorizacionComprobante;
    }

    public void setFechaAutorizacionComprobante(Date fechaAutorizacionComprobante) {
        this.fechaAutorizacionComprobante = fechaAutorizacionComprobante;
    }

    public void setIdComprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public String getEstadoComprobante() {
        return estadoComprobante;
    }

    public void setEstadoComprobante(String estadoComprobante) {
        this.estadoComprobante = estadoComprobante;
    }

    public String getAmbienteComprobante() {
        return ambienteComprobante;
    }

    public void setAmbienteComprobante(String ambienteComprobante) {
        this.ambienteComprobante = ambienteComprobante;
    }

    public Integer getIdentificadorComprobante() {
        return identificadorComprobante;
    }

    public void setIdentificadorComprobante(Integer identificadorComprobante) {
        this.identificadorComprobante = identificadorComprobante;
    }

    public String getMensajeComprobante() {
        return mensajeComprobante;
    }

    public void setMensajeComprobante(String mensajeComprobante) {
        this.mensajeComprobante = mensajeComprobante;
    }

    public String getTipoMensajeComprobante() {
        return tipoMensajeComprobante;
    }

    public void setTipoMensajeComprobante(String tipoMensajeComprobante) {
        this.tipoMensajeComprobante = tipoMensajeComprobante;
    }

    public String getClaveComprobante() {
        return claveComprobante;
    }

    public void setClaveComprobante(String claveComprobante) {
        this.claveComprobante = claveComprobante;
    }

    public String getSecuencialComprobante() {
        return secuencialComprobante;
    }

    public void setSecuencialComprobante(String secuencialComprobante) {
        this.secuencialComprobante = secuencialComprobante;
    }

    public Date getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(Date fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public Date getFechaSistemaComprobante() {
        return fechaSistemaComprobante;
    }

    public void setFechaSistemaComprobante(Date fechaSistemaComprobante) {
        this.fechaSistemaComprobante = fechaSistemaComprobante;
    }

    public BigDecimal getTotalComprobante() {
        return totalComprobante;
    }

    public void setTotalComprobante(BigDecimal totalComprobante) {
        this.totalComprobante = totalComprobante;
    }

    public String getArchivoComprobante() {
        return archivoComprobante;
    }

    public void setArchivoComprobante(String archivoComprobante) {
        this.archivoComprobante = archivoComprobante;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComprobante != null ? idComprobante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprobante)) {
            return false;
        }
        Comprobante other = (Comprobante) object;
        if ((this.idComprobante == null && other.idComprobante != null) || (this.idComprobante != null && !this.idComprobante.equals(other.idComprobante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Comprobante[ idComprobante=" + idComprobante + " ]";
    }

}

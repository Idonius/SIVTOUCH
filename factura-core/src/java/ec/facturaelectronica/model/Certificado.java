/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "certificado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Certificado.findAll", query = "SELECT c FROM Certificado c"),
    @NamedQuery(name = "Certificado.filtered", query = "SELECT c FROM Certificado c"),
    
    
})
public class Certificado implements Serializable{
    @Id
    @SequenceGenerator(sequenceName = "seq_certificado", name = "seq_certificado_gen", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_certificado_gen")
    @Column(name = "id_certificado")
    private Long idCertificado;
    
    @Column(name = "nombre_certificado", length = 30)
    private String nombre;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_ing_certificado")
    private Date fechaIngreso;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_cad_certificado")    
    private Date fechaCaducidad;
    
    @Column(name = "nombre_archivo_certificado", length = 200)
    private String nombreArchivoCertificado;
    
    @JoinColumn(name = "id_estado", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo estado;
    
    @Column(name = "clave_certificado", length = 400)
    private String claveCertificado;
    
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa empresa;
    
    @Transient
    private int diasDisponibles;

    public Long getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(Long idCertificado) {
        this.idCertificado = idCertificado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getNombreArchivoCertificado() {
        return nombreArchivoCertificado;
    }

    public void setNombreArchivoCertificado(String nombreArchivoCertificado) {
        this.nombreArchivoCertificado = nombreArchivoCertificado;
    }

    public Catalogo getEstado() {
        return estado;
    }

    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    public String getClaveCertificado() {
        return claveCertificado;
    }

    public void setClaveCertificado(String claveCertificado) {
        this.claveCertificado = claveCertificado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public int getDiasDisponibles() {
        Calendar calendar = Calendar.getInstance();
        Calendar initialDate = new GregorianCalendar();
        initialDate.setTime(fechaIngreso);
        Calendar finalDate = new GregorianCalendar();
        finalDate.setTime(fechaCaducidad);
        calendar.setTimeInMillis(finalDate.getTime().getTime() - initialDate.getTime().getTime());
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public void setDiasDisponibles(int diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCertificado != null ? idCertificado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Certificado)) {
            return false;
        }
        Certificado other = (Certificado) object;
        if ((this.idCertificado == null && other.idCertificado != null) || (this.idCertificado != null && !this.idCertificado.equals(other.idCertificado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Certificado[ id=" + idCertificado + " ]";
    }
    
    
}

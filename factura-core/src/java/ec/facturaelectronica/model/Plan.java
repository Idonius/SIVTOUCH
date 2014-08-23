/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "plan.findAll", query = "SELECT p FROM Plan p WHERE p.idEstado.nombreCatalogo='Activo' ORDER BY p.costoPlan")
})
public class Plan implements Serializable{
    @Id
    @SequenceGenerator(name = "seq_plan_gen", sequenceName = "seq_plan", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_plan_gen")
    @Column(name = "id_plan")
    private Integer id;
    
    @Column(name = "nombre_plan", length = 100)
    private String nombrePlan; 
    
    @Column(name = "costo_plan", precision = 10, scale = 2)
    private BigDecimal costoPlan;
    
    @Column(name = "valor_factura_plan", precision = 8, scale = 2)
    private BigDecimal valorFacturaPlan;
    
    @JoinColumn(name = "id_estado", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo idEstado;
    
    @Column(name = "min_facturas_plan")
    private int minFacturasPlan; 
    
    @Column(name = "max_facturas_plan")
    private int maxFacturasPlan;
    
    @Column(name = "descripcion_plan", length = 1000)
    private String descripcionPlan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public BigDecimal getCostoPlan() {
        return costoPlan;
    }

    public void setCostoPlan(BigDecimal costoPlan) {
        this.costoPlan = costoPlan;
    }

    public BigDecimal getValorFacturaPlan() {
        return valorFacturaPlan;
    }

    public void setValorFacturaPlan(BigDecimal valorFacturaPlan) {
        this.valorFacturaPlan = valorFacturaPlan;
    }

    public Catalogo getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Catalogo idEstado) {
        this.idEstado = idEstado;
    }
    
    public int getMinFacturasPlan() {
        return minFacturasPlan;
    }

    public void setMinFacturasPlan(int minFacturasPlan) {
        this.minFacturasPlan = minFacturasPlan;
    }

    public int getMaxFacturasPlan() {
        return maxFacturasPlan;
    }

    public void setMaxFacturasPlan(int maxFacturasPlan) {
        this.maxFacturasPlan = maxFacturasPlan;
    }

    public String getDescripcionPlan() {
        return descripcionPlan;
    }

    public void setDescripcionPlan(String descripcionPlan) {
        this.descripcionPlan = descripcionPlan;
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
        if (!(object instanceof Plan)) {
            return false;
        }
        Plan other = (Plan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Plan[ id=" + id + " ]";
    }
    
}

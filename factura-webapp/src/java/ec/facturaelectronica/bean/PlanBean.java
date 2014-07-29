/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.PlanDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Plan;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.PlanService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Armando
 */
@ManagedBean
@ViewScoped
public class PlanBean extends RecursosServices implements Serializable {

    private final int MINIMO_FACTURAS = 1;

    @EJB
    private PlanService planService;

    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginBean;

    private String nombrePlan;
    private BigDecimal costoPlan;
    private BigDecimal valorFacturaPlan;
    private int maxFacturasPlan;
    private String descripcionPlan;
    private List<Plan> planes;
    private PlanDataTableModel planModel;
    private Plan selectedPlan;
    private List<Plan> filterPlan;
    private Plan plan;

    @PostConstruct
    public void init() {
        FacesMessage msg;
        try {
            planes = planService.getTodosLosPlanes();
            planModel = new PlanDataTableModel(planes);
        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("plan.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("fplan:growl");
        }
    }

    public void nuevo() {
        RequestContext.getCurrentInstance().execute("PF('wdgNew').show()");
    }

    public void eliminar() {
        if (selectedPlan != null) {
            plan = selectedPlan;

            RequestContext.getCurrentInstance().execute("PF('confirm').show()");
        }
    }

    private void limpiar() {
        nombrePlan = "";
        maxFacturasPlan = 0;
        costoPlan = BigDecimal.ZERO;
        valorFacturaPlan = BigDecimal.ZERO;
        descripcionPlan = "";
    }

    public void desactivar() {
        FacesMessage msg;

        if (plan != null) {
            try {
                planService.EliminarPlan(plan);
                init();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("plan.header"), recurso.getString("plan.editar.eliminar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
                RequestContext.getCurrentInstance().update("fplan:growl");

            } catch (ServicesException ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("plan.header"), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("fplan:growl");
            }

        }
    }

    public String getNombreEmpresa() {
        FacesMessage msg;
        String result = "";
        try {
            int empresaId = loginBean.getUsuarioLogin().getIdEmpresa().getIdEmpresa();
            Empresa empresa = planService.getEmpresaPorId(empresaId);
            if (empresa != null) {
                result = empresa.getNombreEmpresa();
            }
        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("plan.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("fplan:growl");
        }
        return result;
    }

    public void editar() {
        if (selectedPlan != null) {
            plan = selectedPlan;
            nombrePlan = plan.getNombrePlan();
            maxFacturasPlan = plan.getMaxFacturasPlan();
            costoPlan = plan.getCostoPlan();
            valorFacturaPlan = plan.getValorFacturaPlan();
            descripcionPlan = plan.getDescripcionPlan();

            RequestContext.getCurrentInstance().execute("PF('wdgNew').show()");
        }
    }

    public void guardar() {
        FacesMessage msg;

        try {
            Catalogo catalogo = new Catalogo(EstadosGeneralesEnum.Activo.getOrden());

            if (plan == null) {
                plan = new Plan();
            }

            plan.setNombrePlan(nombrePlan);
            plan.setCostoPlan(costoPlan);
            plan.setValorFacturaPlan(valorFacturaPlan);
            plan.setIdEstado(catalogo);
            plan.setMinFacturasPlan(MINIMO_FACTURAS);
            plan.setMaxFacturasPlan(maxFacturasPlan);
            plan.setDescripcionPlan(descripcionPlan);

            if (plan.getId() == null) {
                planService.nuevoPlan(plan);
            } else {
                planService.modificarPlan(plan);
            }

            init();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("plan.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("fplan:growl");
        } catch (ServicesException ex) {
            plan = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("plan.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("fplan:growl");
        }
    }

    public void cancelar() {
        RequestContext.getCurrentInstance().execute("PF('wdgNew').hide()");
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plan> planes) {
        this.planes = planes;
    }

    public PlanDataTableModel getPlanModel() {
        return planModel;
    }

    public void setPlanModel(PlanDataTableModel planModel) {
        this.planModel = planModel;
    }

    public Plan getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(Plan selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    public List<Plan> getFilterPlan() {
        return filterPlan;
    }

    public void setFilterPlan(List<Plan> filterPlan) {
        this.filterPlan = filterPlan;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setLoginBean(LoginAccessBean loginBean) {
        this.loginBean = loginBean;
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

}

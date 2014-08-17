/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;


import ec.facturaelectronica.datatable.model.EmpresaDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.PlanListModel;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Plan;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.PlanService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author desarrollotic
 */
@ManagedBean(name = "empresaBean")
@ViewScoped
public class EmpresaBean extends RecursosServices implements Serializable {

    @EJB
    AdministracionService admService;
    
    @EJB
    private PlanService planService;
    
    private List<Empresa> listEmpresas;
    private String nombre;
    private String direccion;
    private String telefono;
    private String ruc;
    private List<Empresa> filterEmpresas;
    private Empresa selectedEmpresa;
    private EmpresaDataTableModel empresaModel;
    private Empresa empresa;
    private List<Plan> planes;
    private Plan selectedPlan;

    public EmpresaBean() {
    }

    public List<Empresa> getListEmpresas() {
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresa> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public List<Empresa> getFilterEmpresas() {
        return filterEmpresas;
    }

    public void setFilterEmpresas(List<Empresa> filterEmpresas) {
        this.filterEmpresas = filterEmpresas;
    }

    public Empresa getSelectedEmpresa() {
        return selectedEmpresa;
    }

    public void setSelectedEmpresa(Empresa selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }

    public EmpresaDataTableModel getEmpresaModel() {
        return empresaModel;
    }

    public void setEmpresaModel(EmpresaDataTableModel empresaModel) {
        this.empresaModel = empresaModel;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plan> planes) {
        this.planes = planes;
    }

    public Plan getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(Plan selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    @PostConstruct
    public void initBean() {
        FacesMessage msg;

        try {
            limpiar();
            listEmpresas = admService.getEmpresas();
            empresaModel = new EmpresaDataTableModel(listEmpresas);
            planes = planService.getTodosLosPlanes();
            PlanListModel.planModel = planes;

        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("empresa.header"), ex.getMessage());

            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void nuevo() {

        limpiar();
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
    }

    public void cancelar() {

        limpiar();
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");

    }

    private void limpiar() {
        nombre = "";
        direccion = "";
        telefono = "";
        ruc = "";
        empresa = null;
    }

    public void registrar() {

        FacesMessage msg;

        try {

            if (empresa == null) {
                empresa = new Empresa();
            }

            empresa.setNombreEmpresa(nombre);
            empresa.setDireccionEmpresa(direccion);
            empresa.setRucEmpresa(ruc);
            empresa.setTelefonoEmpresa(telefono);
            empresa.setPlan(selectedPlan);


            if (empresa.getIdEmpresa() == null) {

                admService.guardarEmpresa(empresa);
            } else {
                admService.actualizarEmpresa(empresa);
            }

            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
            initBean();

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("empresa.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (ServicesException ex) {
            empresa = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("empresa.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (Exception ex) {
            empresa = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("empresa.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editar() {

        if (selectedEmpresa != null) {
            empresa = selectedEmpresa;
            nombre = empresa.getNombreEmpresa();
            direccion = empresa.getDireccionEmpresa();
            ruc = empresa.getRucEmpresa();
            telefono = empresa.getTelefonoEmpresa();
            selectedPlan = empresa.getPlan();
            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
        }
    }

    public void eliminar() {

        if (selectedEmpresa != null) {
            empresa = selectedEmpresa;

            RequestContext.getCurrentInstance().execute("PF('confirm').show()");
        }
    }

    public void desactivar() {
        FacesMessage msg;

        if (empresa != null) {
            try {
                admService.eliminarEmpresa(empresa);
                initBean();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("empresa.header"), recurso.getString("empresa.editar.eliminar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
                RequestContext.getCurrentInstance().update("form:growl");

            } catch (ServicesException ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("empresa.header"), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        } 
    }
}

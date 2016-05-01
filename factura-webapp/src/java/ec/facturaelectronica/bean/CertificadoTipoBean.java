/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.constantes.FacturaConstantes;
import ec.facturaelectronica.datatable.model.CertificadoDataTableModel;
import ec.facturaelectronica.datatable.model.CertificadoTipoComprobanteDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.CertificadosListModel;
import ec.facturaelectronica.list.model.EmpresaListModel;
import ec.facturaelectronica.list.model.TipoComprobanteListModel;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author armando
 */
@ManagedBean
@ViewScoped
public class CertificadoTipoBean extends RecursosServices implements Serializable {

    @EJB
    private CertificadoService certificadoService;

    @EJB
    private CertificadoTipoComprobanteService certificadoTipoComprobanteService;

    @EJB
    private AdministracionService admService;

    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginAccessBean;

    private final static Logger LOGGER = LogManager.getLogger();

    private List<Certificado> certificados;
    private Certificado certificadoSelected;

    private List<TipoComprobante> tiposComprobantes;
    private TipoComprobante tipoComprobanteSelected;

    private CertificadoTipoComprobanteDataTableModel tipoComprobanteDataModel;
    private CertificadoTipoComprobante certificadoTipoComprobanteSelected;

    private List<CertificadoTipoComprobante> certificadoTipoComprobantes;

    private CertificadoTipoComprobante certificadoTipoComprobante;

    private List<Empresa> empresas;
    private Empresa selectedEmpresa;
    private Empresa selectedEmpresaBusqueda;
    private boolean isSuperAdm = false;

    @PostConstruct
    public void init() {
        try {

            empresas = admService.getEmpresas();

            if (FacturaConstantes.SUPER_ADMINISTRADOR.equals(loginAccessBean.getUsuarioLogin().getIdPerfil().getNombrePerfil())) {
                certificados = certificadoService.getCertificadosFiltrados(selectedEmpresaBusqueda);
                CertificadosListModel.certificadoModel = certificados;
                isSuperAdm = true;
            } else {
                certificados = certificadoService.getCertificadosFiltrados(loginAccessBean.getUsuarioLogin().getIdEmpresa());
                CertificadosListModel.certificadoModel = certificados;
                isSuperAdm = false;
                selectedEmpresaBusqueda = loginAccessBean.getUsuarioLogin().getIdEmpresa();
            }

            tiposComprobantes = certificadoTipoComprobanteService.obtenerTipoComprobanteList();
            TipoComprobanteListModel.tipoComprobanteModel = tiposComprobantes;
            EmpresaListModel.empresaModel = empresas;
            cargarCertificados(selectedEmpresaBusqueda);

        } catch (ServicesException ex) {
            java.util.logging.Logger.getLogger(CertificadoTipoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seleccionarEmpresa() {

        if (selectedEmpresaBusqueda != null) {
            certificados = certificadoService.getCertificadosFiltrados(selectedEmpresaBusqueda);
            CertificadosListModel.certificadoModel = certificados;
            cargarCertificados(selectedEmpresaBusqueda);

        } else {
            certificados = new ArrayList<>();
            CertificadosListModel.certificadoModel = certificados;
            cargarCertificados(selectedEmpresaBusqueda);
        }
    }

    private void cargarCertificados(Empresa empresa) {
        try {

            certificadoTipoComprobantes = certificadoTipoComprobanteService.obtenerCertificadoTipoComprobanteServiceList(empresa);
            tipoComprobanteDataModel = new CertificadoTipoComprobanteDataTableModel(certificadoTipoComprobantes);

        } catch (ServicesException ex) {
            java.util.logging.Logger.getLogger(CertificadoTipoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editar() {

        if (certificadoTipoComprobanteSelected != null) {
            certificadoTipoComprobante = certificadoTipoComprobanteSelected;
            certificadoSelected = certificadoTipoComprobante.getCertificado();
            tipoComprobanteSelected = certificadoTipoComprobante.getTipoComprobante();
            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
        }
    }

    /**
     * Para la creacion del nuevo certificado
     */
    public void nuevo() {
        FacesMessage msg;
        if (selectedEmpresaBusqueda != null) {
            certificadoSelected = null;
            tipoComprobanteSelected = null;
            certificadoTipoComprobante = new CertificadoTipoComprobante();
            RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"),  recurso.getString("certificadoTipoComprobante.selectempresa.nuevo"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void registrar() {

        FacesMessage msg;
        Catalogo catalogo = new Catalogo(EstadosGeneralesEnum.Activo.getOrden());
        Empresa empresaLogueada = selectedEmpresaBusqueda;

        try {
            if (certificadoTipoComprobanteService.obtenerCertificadoTipoComprobante(empresaLogueada, tipoComprobanteSelected).isEmpty()) {

                certificadoTipoComprobante.setCertificado(certificadoSelected);
                certificadoTipoComprobante.setTipoComprobante(tipoComprobanteSelected);
                certificadoTipoComprobante.setCatalogo(catalogo);
                certificadoTipoComprobante.setEmpresa(empresaLogueada);
                certificadoTipoComprobanteService.agregarCertificadoTipoComprobanteService(certificadoTipoComprobante);

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("editar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
                certificadoTipoComprobante = new CertificadoTipoComprobante();
                cargarCertificados(empresaLogueada);

            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("certificadoTipoComprobante.editar.mensaje.ya.existe"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        } catch (ServicesException ex) {
            certificadoTipoComprobante = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (Exception ex) {
            certificadoTipoComprobante = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void cancelar() {
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
    }

    /**
     * Para la eliminacion del certificado elegido
     */
    public void desactivar() {
        FacesMessage msg;

        if (certificadoTipoComprobante != null) {
            try {
                certificadoTipoComprobanteService.eliminarCertificadoTipoComprobanteService(certificadoTipoComprobante);

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("certificadoTipoComprobante.editar.eliminar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                cargarCertificados(selectedEmpresaBusqueda);
                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
            } catch (Exception ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        }
    }

    public void eliminar() {

        if (certificadoTipoComprobanteSelected != null) {
            certificadoTipoComprobante = certificadoTipoComprobanteSelected;

            RequestContext.getCurrentInstance().execute("PF('confirm').show()");
        }
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    public Certificado getCertificadoSelected() {
        return certificadoSelected;
    }

    public void setCertificadoSelected(Certificado certificadoSelected) {
        this.certificadoSelected = certificadoSelected;
    }

    public List<TipoComprobante> getTiposComprobantes() {
        return tiposComprobantes;
    }

    public void setTiposComprobantes(List<TipoComprobante> tiposComprobantes) {
        this.tiposComprobantes = tiposComprobantes;
    }

    public TipoComprobante getTipoComprobanteSelected() {
        return tipoComprobanteSelected;
    }

    public void setTipoComprobanteSelected(TipoComprobante tipoComprobanteSelected) {
        this.tipoComprobanteSelected = tipoComprobanteSelected;
    }

    public CertificadoTipoComprobanteDataTableModel getTipoComprobanteDataModel() {
        return tipoComprobanteDataModel;
    }

    public void setTipoComprobanteDataModel(CertificadoTipoComprobanteDataTableModel tipoComprobanteDataModel) {
        this.tipoComprobanteDataModel = tipoComprobanteDataModel;
    }

    public CertificadoTipoComprobante getCertificadoTipoComprobanteSelected() {
        return certificadoTipoComprobanteSelected;
    }

    public void setCertificadoTipoComprobanteSelected(CertificadoTipoComprobante certificadoTipoComprobanteSelected) {
        this.certificadoTipoComprobanteSelected = certificadoTipoComprobanteSelected;
    }

    public List<CertificadoTipoComprobante> getCertificadoTipoComprobantes() {
        return certificadoTipoComprobantes;
    }

    public void setCertificadoTipoComprobantes(List<CertificadoTipoComprobante> certificadoTipoComprobantes) {
        this.certificadoTipoComprobantes = certificadoTipoComprobantes;
    }

    public void setLoginAccessBean(LoginAccessBean loginAccessBean) {
        this.loginAccessBean = loginAccessBean;
    }

    /**
     * @return the empresas
     */
    public List<Empresa> getEmpresas() {
        return empresas;
    }

    /**
     * @param empresas the empresas to set
     */
    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    /**
     * @return the selectedEmpresa
     */
    public Empresa getSelectedEmpresa() {
        return selectedEmpresa;
    }

    /**
     * @param selectedEmpresa the selectedEmpresa to set
     */
    public void setSelectedEmpresa(Empresa selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }

    /**
     * @return the selectedEmpresaBusqueda
     */
    public Empresa getSelectedEmpresaBusqueda() {
        return selectedEmpresaBusqueda;
    }

    /**
     * @param selectedEmpresaBusqueda the selectedEmpresaBusqueda to set
     */
    public void setSelectedEmpresaBusqueda(Empresa selectedEmpresaBusqueda) {
        this.selectedEmpresaBusqueda = selectedEmpresaBusqueda;
    }

    /**
     * @return the isSuperAdm
     */
    public boolean isIsSuperAdm() {
        return isSuperAdm;
    }

    /**
     * @param isSuperAdm the isSuperAdm to set
     */
    public void setIsSuperAdm(boolean isSuperAdm) {
        this.isSuperAdm = isSuperAdm;
    }

}

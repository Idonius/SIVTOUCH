/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.CertificadoTipoComprobanteDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.CertificadosListModel;
import ec.facturaelectronica.list.model.TipoComprobanteListModel;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
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
    private List<CertificadoTipoComprobante> filterCertificadosTipo;

    private CertificadoTipoComprobante certificadoTipoComprobante;

    @PostConstruct
    public void init() {
        try {
            LOGGER.error("Entre aqui");
            certificados = certificadoService.getCertificadosFiltrados();
            CertificadosListModel.certificadoModel = certificados;

            tiposComprobantes = certificadoTipoComprobanteService.obtenerTipoComprobanteList();
            TipoComprobanteListModel.tipoComprobanteModel = tiposComprobantes;

            certificadoTipoComprobantes = certificadoTipoComprobanteService.obtenerCertificadoTipoComprobanteServiceList();
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
        certificadoSelected = null;
        tipoComprobanteSelected = null;
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
    }

    public void registrar() {

        FacesMessage msg;
        Catalogo catalogo = new Catalogo(EstadosGeneralesEnum.Activo.getOrden());
        Empresa empresaLogueada = loginAccessBean.getUsuarioLogin().getIdEmpresa();

        try {
            if (certificadoTipoComprobanteService.obtenerCertificadoTipoComprobante(empresaLogueada, tipoComprobanteSelected).isEmpty()) {
                if (certificadoTipoComprobante == null) {
                    certificadoTipoComprobante = new CertificadoTipoComprobante();
                }

                certificadoTipoComprobante.setCertificado(certificadoSelected);
                certificadoTipoComprobante.setTipoComprobante(tipoComprobanteSelected);
                certificadoTipoComprobante.setCatalogo(catalogo);

                if (certificadoTipoComprobante.getId() == null) {

                    certificadoTipoComprobanteService.agregarCertificadoTipoComprobanteService(certificadoTipoComprobante);
                } else {
                    certificadoTipoComprobanteService.actualizarCertificadoTipoComprobanteService(certificadoTipoComprobante);
                }

                RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
                init();

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("empresa.header"), recurso.getString("editar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }else{
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("empresa.header"), recurso.getString("certificadoTipoComprobante.editar.mensaje.ya.existe"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");                
            }

        } catch (ServicesException ex) {
            certificadoTipoComprobante = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("empresa.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        } catch (Exception ex) {
            certificadoTipoComprobante = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("empresa.header"), ex.getMessage());
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
                init();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("certificadoTipoComprobante.editar.eliminar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
                RequestContext.getCurrentInstance().update("form:growl");

            } catch (ServicesException ex) {
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

    public List<CertificadoTipoComprobante> getFilterCertificadosTipo() {
        return filterCertificadosTipo;
    }

    public void setFilterCertificadosTipo(List<CertificadoTipoComprobante> filterCertificadosTipo) {
        this.filterCertificadosTipo = filterCertificadosTipo;
    }

    public void setLoginAccessBean(LoginAccessBean loginAccessBean) {
        this.loginAccessBean = loginAccessBean;
    }

}

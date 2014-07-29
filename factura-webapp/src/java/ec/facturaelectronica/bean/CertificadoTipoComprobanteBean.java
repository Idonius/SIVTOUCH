/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.CertificadoTipoComprobanteDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Armando
 */
@ManagedBean
@ViewScoped
public class CertificadoTipoComprobanteBean extends RecursosServices implements Serializable {

    @EJB
    private CertificadoTipoComprobanteService certificadoTipoComprobanteService;

    @EJB
    private CertificadoService certificadoService;

    private List<CertificadoTipoComprobante> certificadoTipoComprobanteList;
    private CertificadoTipoComprobanteDataTableModel certificadoTipoComprobanteDataTableModel;
    private CertificadoTipoComprobante selectedCertificadoTipoComprobante;
    private List<CertificadoTipoComprobante> filterCertificadoTipoComprobante;
    private Certificado selectedCertificado;
    private List<Certificado> certificados;
    private TipoComprobante selectedTipoComprobante;
    private List<TipoComprobante> tiposComprobante;
    private CertificadoTipoComprobante certificadoTipoComprobante;

    @PostConstruct
    public void init() {
        FacesMessage msg;
        try {
            certificados = certificadoService.getCertificadosFiltrados();
            tiposComprobante = certificadoTipoComprobanteService.obtenerTipoComprobanteList();            
            certificadoTipoComprobanteList = certificadoTipoComprobanteService.obtenerCertificadoTipoComprobanteServiceList();
            LOG.error(certificadoTipoComprobanteList.size());
            certificadoTipoComprobanteDataTableModel = new CertificadoTipoComprobanteDataTableModel(certificadoTipoComprobanteList);
        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("formId:growl");
        }

    }

    public void nuevo() {
        RequestContext.getCurrentInstance().execute("PF('wdgEdit').show()");
    }

    public void editar() {
        if (selectedCertificadoTipoComprobante != null) {
            certificadoTipoComprobante = selectedCertificadoTipoComprobante;
            selectedCertificado = certificadoTipoComprobante.getCertificado();
            selectedTipoComprobante = certificadoTipoComprobante.getTipoComprobante();

            RequestContext.getCurrentInstance().execute("PF('wdgEdit').show()");
        }
    }

    public void eliminar() {
        if (selectedCertificadoTipoComprobante != null) {
            certificadoTipoComprobante = selectedCertificadoTipoComprobante;

            RequestContext.getCurrentInstance().execute("PF('confirm').show()");
        }
    }
    
    public void desactivar() {
        FacesMessage msg;

        if (certificadoTipoComprobante != null) {
            try {
                certificadoTipoComprobanteService.eliminarCertificadoTipoComprobanteService(certificadoTipoComprobante);
                init();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("certificadoTipoComprobante.editar.eliminar.mensaje"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
                RequestContext.getCurrentInstance().update("formId:growl");

            } catch (ServicesException ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("formId:growl");
            }

        } 
    }
    

    public void guardar() {
        FacesMessage msg;
        
        try {
            Catalogo catalogo = new Catalogo(EstadosGeneralesEnum.Activo.getOrden());
            LOG.error("Certificado seleccionado: " + selectedCertificado);
            if (certificadoTipoComprobante == null) {
                certificadoTipoComprobante = new CertificadoTipoComprobante();
            }

            certificadoTipoComprobante.setCertificado(selectedCertificado);
            certificadoTipoComprobante.setTipoComprobante(selectedTipoComprobante);
            certificadoTipoComprobante.setCatalogo(catalogo);

            if (certificadoTipoComprobante.getId() == null) {

                certificadoTipoComprobanteService.agregarCertificadoTipoComprobanteService(certificadoTipoComprobante);
            } else {
                certificadoTipoComprobanteService.actualizarCertificadoTipoComprobanteService(certificadoTipoComprobante);
            }

            RequestContext.getCurrentInstance().execute("PF('wdgEdit').hide()");
            init();

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("editar.mensaje"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("formId:growl");

        } catch (ServicesException ex) {
            certificadoTipoComprobante = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("formId:growl");

        } catch (Exception ex) {
            certificadoTipoComprobante = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("formId:growl");
        }

    }

    public List<CertificadoTipoComprobante> getCertificadoTipoComprobanteList() {
        return certificadoTipoComprobanteList;
    }

    public void setCertificadoTipoComprobanteList(List<CertificadoTipoComprobante> certificadoTipoComprobanteList) {
        this.certificadoTipoComprobanteList = certificadoTipoComprobanteList;
    }

    public CertificadoTipoComprobanteDataTableModel getCertificadoTipoComprobanteDataTableModel() {
        return certificadoTipoComprobanteDataTableModel;
    }

    public void setCertificadoTipoComprobanteDataTableModel(CertificadoTipoComprobanteDataTableModel certificadoTipoComprobanteDataTableModel) {
        this.certificadoTipoComprobanteDataTableModel = certificadoTipoComprobanteDataTableModel;
    }

    public CertificadoTipoComprobante getSelectedCertificadoTipoComprobante() {
        return selectedCertificadoTipoComprobante;
    }

    public void setSelectedCertificadoTipoComprobante(CertificadoTipoComprobante selectedCertificadoTipoComprobante) {
        this.selectedCertificadoTipoComprobante = selectedCertificadoTipoComprobante;
    }

    public List<CertificadoTipoComprobante> getFilterCertificadoTipoComprobante() {
        return filterCertificadoTipoComprobante;
    }

    public void setFilterCertificadoTipoComprobante(List<CertificadoTipoComprobante> filterCertificadoTipoComprobante) {
        this.filterCertificadoTipoComprobante = filterCertificadoTipoComprobante;
    }

    public Certificado getSelectedCertificado() {
        return selectedCertificado;
    }

    public void setSelectedCertificado(Certificado selectedCertificado) {
        this.selectedCertificado = selectedCertificado;
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    public TipoComprobante getSelectedTipoComprobante() {
        return selectedTipoComprobante;
    }

    public void setSelectedTipoComprobante(TipoComprobante selectedTipoComprobante) {
        this.selectedTipoComprobante = selectedTipoComprobante;
    }

    public List<TipoComprobante> getTiposComprobante() {
        return tiposComprobante;
    }

    public void setTiposComprobante(List<TipoComprobante> tiposComprobante) {
        this.tiposComprobante = tiposComprobante;
    }

    public CertificadoTipoComprobante getCertificadoTipoComprobante() {
        return certificadoTipoComprobante;
    }

    public void setCertificadoTipoComprobante(CertificadoTipoComprobante certificadoTipoComprobante) {
        this.certificadoTipoComprobante = certificadoTipoComprobante;
    }

}

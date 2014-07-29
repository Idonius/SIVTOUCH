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
@ManagedBean(name = "certificadoTipoComprobanteBean")
@ViewScoped
public class CertificadoTipoComprobanteBean extends RecursosServices implements Serializable {

    @EJB
    private CertificadoTipoComprobanteService certificadoTipoComprobanteService;

    @EJB
    private CertificadoService certificadoService;

    //private List<CertificadoTipoComprobante> certificadoTipoComprobanteList;
    //private CertificadoTipoComprobanteDataTableModel certificadoTipoComprobanteDataTableModel;
    //private CertificadoTipoComprobante selectedCertificadoTipoComprobante;
    //private List<CertificadoTipoComprobante> filterCertificadoTipoComprobante;
    private Certificado selectedCertificado;
    private List<Certificado> listaCertificados;
    //private TipoComprobante selectedTipoComprobante;
    //private List<TipoComprobante> tiposComprobante;
    //private CertificadoTipoComprobante certificadoTipoComprobante;

    @PostConstruct
    public void init() {
        FacesMessage msg;
        try {

            listaCertificados = certificadoService.getCertificadosFiltrados();

            //setListaCertificados(certificadoService.getCertificadosFiltrados());
            //      tiposComprobante = certificadoTipoComprobanteService.obtenerTipoComprobanteList(); 
            CertificadosListModel.certificadoModel = listaCertificados;
        //    TipoComprobanteListModel.tipoComprobanteModel=tiposComprobante;

            //  certificadoTipoComprobanteList = certificadoTipoComprobanteService.obtenerCertificadoTipoComprobanteServiceList();
            //LOG.error(certificadoTipoComprobanteList.size());
            //certificadoTipoComprobanteDataTableModel = new CertificadoTipoComprobanteDataTableModel(certificadoTipoComprobanteList);
        } catch (Exception ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("formId:growl");
        }

    }

    public void nuevo() {
        RequestContext.getCurrentInstance().execute("PF('wdgEdit').show()");
    }

//    public void editar() {
//        if (selectedCertificadoTipoComprobante != null) {
//            certificadoTipoComprobante = selectedCertificadoTipoComprobante;
//            setSelectedCertificado(certificadoTipoComprobante.getCertificado());
//            selectedTipoComprobante = certificadoTipoComprobante.getTipoComprobante();
//
//            RequestContext.getCurrentInstance().execute("PF('wdgEdit').show()");
//        }
//    }
    public void validar() {
        System.err.println(getSelectedCertificado());
    }

//    public void eliminar() {
//        if (selectedCertificadoTipoComprobante != null) {
//            certificadoTipoComprobante = selectedCertificadoTipoComprobante;
//
//            RequestContext.getCurrentInstance().execute("PF('confirm').show()");
//        }
//    }
//    public void desactivar() {
//        FacesMessage msg;
//
//        if (certificadoTipoComprobante != null) {
//            try {
//                certificadoTipoComprobanteService.eliminarCertificadoTipoComprobanteService(certificadoTipoComprobante);
//                init();
//                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("certificadoTipoComprobante.editar.eliminar.mensaje"));
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//                RequestContext.getCurrentInstance().execute("PF('confirm').hide()");
//                RequestContext.getCurrentInstance().update("formId:growl");
//
//            } catch (ServicesException ex) {
//                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//                RequestContext.getCurrentInstance().update("formId:growl");
//            }
//
//        }
//    }
//    public void guardar() {
//        FacesMessage msg;
//
//        try {
//            Catalogo catalogo = new Catalogo(EstadosGeneralesEnum.Activo.getOrden());
//            LOG.error("Certificado seleccionado: " + getSelectedCertificado());
//            if (certificadoTipoComprobante == null) {
//                certificadoTipoComprobante = new CertificadoTipoComprobante();
//            }
//
//            certificadoTipoComprobante.setCertificado(getSelectedCertificado());
//            certificadoTipoComprobante.setTipoComprobante(selectedTipoComprobante);
//            certificadoTipoComprobante.setCatalogo(catalogo);
//
//            if (certificadoTipoComprobante.getId() == null) {
//
//                certificadoTipoComprobanteService.agregarCertificadoTipoComprobanteService(certificadoTipoComprobante);
//            } else {
//                certificadoTipoComprobanteService.actualizarCertificadoTipoComprobanteService(certificadoTipoComprobante);
//            }
//
//            RequestContext.getCurrentInstance().execute("PF('wdgEdit').hide()");
//            init();
//
//            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificadoTipoComprobante.header"), recurso.getString("editar.mensaje"));
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("formId:growl");
//
//        } catch (ServicesException ex) {
//            certificadoTipoComprobante = null;
//            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("formId:growl");
//
//        } catch (Exception ex) {
//            certificadoTipoComprobante = null;
//            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("certificadoTipoComprobante.header"), ex.getMessage());
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("formId:growl");
//        }
//
//    }
    /**
     * @return the selectedCertificado
     */
    public Certificado getSelectedCertificado() {
        return selectedCertificado;
    }

    /**
     * @param selectedCertificado the selectedCertificado to set
     */
    public void setSelectedCertificado(Certificado selectedCertificado) {
        this.selectedCertificado = selectedCertificado;
    }

    /**
     * @return the listaCertificados
     */
    public List<Certificado> getListaCertificados() {
        return listaCertificados;
    }

    /**
     * @param listaCertificados the listaCertificados to set
     */
    public void setListaCertificados(List<Certificado> listaCertificados) {
        this.listaCertificados = listaCertificados;
    }
}

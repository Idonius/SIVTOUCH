/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.CertificadoTipoComprobanteDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Christian
 */
@ManagedBean
@ViewScoped
public class CertificadoTipoBean implements Serializable {

    /**
     * Creates a new instance of CertificadoTipoBean
     */
    @EJB
    private CertificadoService certificadoService;
    
    @EJB
    private CertificadoTipoComprobanteService certificadoTipoComprobanteService;

    private List<Certificado> listaCertificados;
    private List<CertificadoTipoComprobante> listaTipoCertificado;
    private List<TipoComprobante> listaTipoComprobante;
    private CertificadoTipoComprobante selectedCertificado;
    private TipoComprobante tipoComprobanteSelected;
    private CertificadoTipoComprobanteDataTableModel certificadoModel;

    public CertificadoTipoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            System.err.println("Entre en init");
            listaCertificados = certificadoService.getCertificadosFiltrados();
            listaTipoComprobante = certificadoTipoComprobanteService.obtenerTipoComprobanteList();
            
            listaTipoCertificado = certificadoTipoComprobanteService.obtenerCertificadoTipoComprobanteServiceList();
            certificadoModel = new CertificadoTipoComprobanteDataTableModel(listaTipoCertificado);
                       
        } catch (ServicesException ex) {
            Logger.getLogger(CertificadoTipoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editar(){
        System.err.println("Dame el certificado selecto: " + selectedCertificado);
        System.err.println("Dame el tipo comprobante selecto: " + tipoComprobanteSelected);
    }
    
    public void eliminar(){
        
    }

    public void nuevo() {
        RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').show()");
    }
    
    public void cancelar(){
        
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

    /**
     * @return the selectedCertificado
     */
    public CertificadoTipoComprobante getSelectedCertificado() {
        return selectedCertificado;
    }

    /**
     * @param selectedCertificado the selectedCertificado to set
     */
    public void setSelectedCertificado(CertificadoTipoComprobante selectedCertificado) {
        this.selectedCertificado = selectedCertificado;
    }

    public CertificadoTipoComprobanteDataTableModel getCertificadoModel() {
        return certificadoModel;
    }

    public void setCertificadoModel(CertificadoTipoComprobanteDataTableModel certificadoModel) {
        this.certificadoModel = certificadoModel;
    }

    public List<CertificadoTipoComprobante> getListaTipoCertificado() {
        return listaTipoCertificado;
    }

    public void setListaTipoCertificado(List<CertificadoTipoComprobante> listaTipoCertificado) {
        this.listaTipoCertificado = listaTipoCertificado;
    }

    public List<TipoComprobante> getListaTipoComprobante() {
        return listaTipoComprobante;
    }

    public void setListaTipoComprobante(List<TipoComprobante> listaTipoComprobante) {
        this.listaTipoComprobante = listaTipoComprobante;
    }

    public TipoComprobante getTipoComprobanteSelected() {
        return tipoComprobanteSelected;
    }

    public void setTipoComprobanteSelected(TipoComprobante tipoComprobanteSelected) {
        this.tipoComprobanteSelected = tipoComprobanteSelected;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.list.model.CertificadosListModel;
import ec.facturaelectronica.list.model.PlanListModel;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.service.CertificadoService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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

    private List<Certificado> listaCertificados;
    private Certificado selectedCertificado;

    public CertificadoTipoBean() {
    }

    @PostConstruct
    public void init() {
        System.err.println("entra init");
        listaCertificados = certificadoService.getCertificadosFiltrados();
        CertificadosListModel.certificadoModel = listaCertificados;
    }

    public void validar() {
        System.err.println(selectedCertificado);
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
    public Certificado getSelectedCertificado() {
        return selectedCertificado;
    }

    /**
     * @param selectedCertificado the selectedCertificado to set
     */
    public void setSelectedCertificado(Certificado selectedCertificado) {
        this.selectedCertificado = selectedCertificado;
    }

}

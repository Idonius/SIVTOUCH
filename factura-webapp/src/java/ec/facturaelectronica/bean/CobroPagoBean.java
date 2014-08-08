/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.PagoDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.CatalogoListModel;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.service.CobroPagoService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Armando
 */
@ManagedBean
@ViewScoped
public class CobroPagoBean extends RecursosServices implements Serializable{
    
    @EJB
    private CobroPagoService cobroPagoService;
    
    private PagoDataTableModel pagoModel;
    private List<Pago> pagos;
    private List<Catalogo> catalogos;
    private Pago pagoSelected;
    private List<Pago> filterPagos;
    
    @PostConstruct
    public void init(){
        try {
            pagos = cobroPagoService.obtenerPagosGenerados();
            pagoModel = new PagoDataTableModel(pagos);
            catalogos = cobroPagoService.obtenerCatalogoTrans();
            CatalogoListModel.catalogoModel = catalogos;
        } catch (ServicesException ex) {
            errorMessages("Pagos y Cobros", ex.getMessage(), ":frmCobros:growl");
        }
    }
    
    public void validarPago(){
        
    }

    
//    Getters & Setters
    public PagoDataTableModel getPagoModel() {
        return pagoModel;
    }

    public void setPagoModel(PagoDataTableModel pagoModel) {
        this.pagoModel = pagoModel;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<Catalogo> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }

    public Pago getPagoSelected() {
        return pagoSelected;
    }

    public void setPagoSelected(Pago pagoSelected) {
        this.pagoSelected = pagoSelected;
    }

    public List<Pago> getFilterPagos() {
        return filterPagos;
    }

    public void setFilterPagos(List<Pago> filterPagos) {
        this.filterPagos = filterPagos;
    }
    
}

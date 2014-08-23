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
import ec.facturaelectronica.model.CobroPago;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.service.CobroPagoService;
import ec.facturaelectronica.service.GeneraPagoService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Armando
 */
@ManagedBean(name = "cobrosBean")
@ViewScoped
public class CobrosBean extends RecursosServices implements Serializable{
    
    @EJB
    private CobroPagoService cobroPagoService;
    
    @EJB
    private GeneraPagoService pagoService;
    
    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginAccessBean;    
    
    private List<Pago> pagos; 
    private PagoDataTableModel pagoModel;
    private Pago pagoSelected;
    private List<Catalogo> catalogos;
    private Catalogo catalogoSelected;
    private Date fechaTransaccion;
    private String numeroTransaccion;    
    private String nroFactura;
    private String observaciones;
    
    public CobrosBean() {
    }
    
    @PostConstruct
    public void init(){
        try {
            pagos = cobroPagoService.obtenerPagosGeneradosService();
            pagoModel = new PagoDataTableModel(pagos);
            catalogos = cobroPagoService.obtenerCatalogosPorGrupoTransacciones();
            CatalogoListModel.catalogoModel = catalogos;
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("cobro.header"), ex.getMessage(), ":fCobros:growl");
        }       
    }
    
    public void validarPago(final ActionEvent evt){
        RequestContext.getCurrentInstance().execute("PF('editarCobros').show()");
    }
    
    public void registrar(){
        Calendar calendar = Calendar.getInstance();
        try {
//            Modificar en Tabla pago
            pagoSelected.setUsuarioApruebaPago(loginAccessBean.getUsuarioLogin());
            pagoSelected.setFechaApruebaPago(calendar.getTime());
            pagoService.actualizarPago(pagoSelected);
//            Insertar Tabla cobro-pago
            CobroPago cobro = new CobroPago();
            cobro.setIdPago(pagoSelected);
            cobro.setFacturaCobro(Integer.parseInt(nroFactura));
            cobro.setFechaTransaccionCobro(fechaTransaccion);
            cobro.setTipoTransaccion(catalogoSelected);
            cobro.setNumeroTransaccionCobro(numeroTransaccion);
            cobroPagoService.guardarCobroPago(cobro);
            infoMessages(recurso.getString("cobro.header"), recurso.getString("editar.mensaje"), ":fCobros:growl");
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("cobro.header"), ex.getMessage(), ":fCobros:growl");
        }
    }
    
    public void cancelarPago(final ActionEvent evt){
        RequestContext.getCurrentInstance().execute("PF('ventanaCancelar').show()");
    }
    
    public void cancel(ActionEvent event){
        RequestContext.getCurrentInstance().execute("PF('editarCobros').hide()");
    }
    
    /**
     * Persistir datos de cancelacion en entidad Pago
     */
    public void cancelar(final ActionEvent evnt){
        try {
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            Calendar calendar = Calendar.getInstance();
            Catalogo catalogo = new Catalogo(4L);
      
            pagoSelected.setUsuarioCancelaPago(loginAccessBean.getUsuarioLogin());
            pagoSelected.setEstado(catalogo);
            pagoSelected.setFechaCancelaPago(calendar.getTime());
            pagoSelected.setObservacionesPago(observaciones);
            pagoService.actualizarPago(pagoSelected);
            infoMessages(recurso.getString("cobro.header"), recurso.getString("editar.mensaje.cancelacion"), ":fCobros:growl");
            init();
        } catch (Exception ex) {
            errorMessages(recurso.getString("cobro.header"), ex.getMessage(), ":fCobros:growl");
        }
    }
    
    /**
     * Cerrar popup cancelar pago
     */
    public void cerrar(){
        RequestContext.getCurrentInstance().execute("PF('ventanaCancelar').hide()");
    }
    
    //Getters & Setters
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

    public Pago getPagoSelected() {
        return pagoSelected;
    }

    public void setPagoSelected(Pago pagoSelected) {
        this.pagoSelected = pagoSelected;
    }

    public List<Catalogo> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }    

    public Catalogo getCatalogoSelected() {
        return catalogoSelected;
    }

    public void setCatalogoSelected(Catalogo catalogoSelected) {
        this.catalogoSelected = catalogoSelected;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setLoginAccessBean(LoginAccessBean loginAccessBean) {
        this.loginAccessBean = loginAccessBean;
    }    
    
}

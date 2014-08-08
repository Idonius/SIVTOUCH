/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.PagoDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.TipoTransaccionListModel;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.CobroPago;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.CobroPagoService;
import ec.facturaelectronica.service.GeneraPagoService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author armando
 */
@ManagedBean
@ViewScoped
public class CobroPagoBean extends RecursosServices implements Serializable{
    
    private final static Logger LOGGER = LogManager.getLogger();
    private final Long ESTADO_PAGADO = 5L;
    private final Long ESTADO_CANCELADO = 4L;
    
    @EJB
    private CobroPagoService cobroPagoService;
    
    @EJB
    private GeneraPagoService pagoService;
    
    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginAccessBean;
    
    
    private List<Pago> pagos;
    private PagoDataTableModel pagoModel;
    private Pago pagoSelected;
    private List<Pago> pagosFiltered;
    private Usuario userLogged;
    private CobroPago cobroPago;
    private List<Catalogo> tipoTransaccionList;
    private String observaciones;
    private Date fechaTransaccion;
    private String numeroTransaccion;
    private Integer facturaCobro;
    private Catalogo tipoTransaccion;
    
    @PostConstruct
    public void init(){
        try {
            pagos = cobroPagoService.obtenerPagosGeneradosService();
            pagoModel = new PagoDataTableModel(pagos);
            tipoTransaccionList = cobroPagoService.obtenerCatalogosPorGrupoTransacciones();
            TipoTransaccionListModel.catalogoModel = tipoTransaccionList;
        } catch (ServicesException ex) {
            errorMessages("Cobros y Pagos", ex.getMessage(), ":frmCobros:growl");
        }
    }
    
    public void validarPago(final ActionEvent evt){
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').show()");
    }
    
    public void cancellPago(final ActionEvent evt){
        RequestContext.getCurrentInstance().execute("PF('ventanaCancelar').show()");
    }
    
    
    public void registrar(final ActionEvent evt){
        try {
            Catalogo catalogo = new Catalogo(ESTADO_PAGADO);
            
            setUserLogged(loginAccessBean.getUsuarioLogin());
            pagoSelected.setUsuarioApruebaPago(getUserLogged());
            pagoSelected.setFechaApruebaPago(new Date());
            pagoSelected.setEstado(catalogo);
            pagoService.actualizarPago(pagoSelected);
            
            
            LOG.error("Transaccion: " + getNumeroTransaccion());
            LOG.error("IdTransaccion: " + getTipoTransaccion());
            LOG.error("Fecha: " + getFechaTransaccion());

            cobroPago = new CobroPago();
            cobroPago.setIdPago(pagoSelected);
            cobroPago.setTipoTransaccion(getTipoTransaccion());
            cobroPago.setNumeroTransaccionCobro(getNumeroTransaccion());
            cobroPago.setFechaTransaccionCobro(getFechaTransaccion());
            cobroPago.setFacturaCobro(getFacturaCobro());
            cobroPagoService.guardarCobroPago(cobroPago);
            init();
//        TODO Arreglar info
            infoMessages("Cobros y Pagos", "El pago se ha realizado con exito", ":frmCobros:growl");
        } catch (ServicesException ex) {
            errorMessages("Cobros y Pagos", ex.getMessage(), ":frmCobros:growl");
        }
        
    }
    
    public void cancelarPago(final ActionEvent evt){
            Catalogo catalogo = new Catalogo(ESTADO_CANCELADO);
            
            setUserLogged(loginAccessBean.getUsuarioLogin());
            pagoSelected.setUsuarioApruebaPago(getUserLogged());
            pagoSelected.setFechaCancelaPago(new Date());
            pagoSelected.setEstado(catalogo);
            pagoSelected.setObservacionesPago(observaciones);
            pagoService.actualizarPago(pagoSelected);
            init();
    }
    
    public void cancelar(){
        RequestContext.getCurrentInstance().execute("PF('ventanaEditar').hide()");
    }
    
//    Getters & Setters
    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public PagoDataTableModel getPagoModel() {
        return pagoModel;
    }

    public void setPagoModel(PagoDataTableModel pagoModel) {
        this.pagoModel = pagoModel;
    }

    public Pago getPagoSelected() {
        return pagoSelected;
    }

    public void setPagoSelected(Pago pagoSelected) {
        this.pagoSelected = pagoSelected;
    }

    public List<Pago> getPagosFiltered() {
        return pagosFiltered;
    }

    public void setPagosFiltered(List<Pago> pagosFiltered) {
        this.pagosFiltered = pagosFiltered;
    }

    public void setLoginAccessBean(LoginAccessBean loginAccessBean) {
        this.loginAccessBean = loginAccessBean;
    }    

    public Usuario getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(Usuario userLogged) {
        this.userLogged = userLogged;
    }

    public CobroPago getCobroPago() {
        return cobroPago;
    }

    public void setCobroPago(CobroPago cobroPago) {
        this.cobroPago = cobroPago;
    }

    public List<Catalogo> getTipoTransaccionList() {
        return tipoTransaccionList;
    }

    public void setTipoTransaccionList(List<Catalogo> tipoTransaccionList) {
        this.tipoTransaccionList = tipoTransaccionList;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public Integer getFacturaCobro() {
        return facturaCobro;
    }

    public void setFacturaCobro(Integer facturaCobro) {
        this.facturaCobro = facturaCobro;
    }

    public Catalogo getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(Catalogo tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

}

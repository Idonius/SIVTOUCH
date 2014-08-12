/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.ComprobanteDataTableModel;
import ec.facturaelectronica.datatable.model.TipoComprobanteDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.EmpresaListModel;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.service.GeneraPagoService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Armando
 */
@ManagedBean
@ViewScoped
public class PagoBean extends RecursosServices implements Serializable {

    private final String FORMATO_FECHA = "dd-MM-yyyy";
    private final String COMPROBANTE_ESTADO = "AUTORIZADO";

    @EJB
    private GeneraPagoService pagoService;
    
    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginAccessBean;

    private Empresa empresaSelected;
    private List<Empresa> empresas;
    private Date fechaGeneracion;
    private Pago pago;
    private boolean visible = false;
    private ComprobanteDataTableModel comprobanteModel;
    private Comprobante comprobanteSelected;
    private List<Comprobante> comprobanteFiltered;
    private List<Comprobante> comprobantes;
    private TipoComprobanteDataTableModel tipoComprobanteModel;
    private List<TipoComprobante> tiposConmprobante;
    private TipoComprobante tipoComprobanteSelected;
    private List<TipoComprobante> tiposComprobanteFiltered;
    private int totalComprobantes;

    @PostConstruct
    public void init() {
        try {
            empresas = pagoService.obtenerTodasLasEmpresasActivas();
            EmpresaListModel.empresaModel = empresas;
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("pago.header"), ex.getMessage(), "form:growl");
        }
    }

    private String getNominacionMes(int mes) {
        switch (mes) {
            case 1:
                return "ENERO";
            case 2:
                return "FEBRERO";
            case 3:
                return "MARZO";
            case 4:
                return "ABRIL";
            case 5:
                return "MAYO";
            case 6:
                return "JUNIO";
            case 7:
                return "JULIO";
            case 8:
                return "AGOSTO";
            case 9:
                return "SEPTIEMBRE";
            case 10:
                return "OCTUBRE";
            case 11:
                return "NOVIEMBRE";
            default:
                return "DICIEMBRE";
        }
    }

    public String getMes() {
        return getNominacionMes(pago.getMesPago());
    }

    public void onDateSelect(SelectEvent evt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        fechaGeneracion = sdf.parse(sdf.format(evt.getObject()));
    }

    public void generar() throws ServicesException {
        if(totalComprobantes == 0){
            setTotalComprobantes(pagoService.obtenerLosComprobantesPorEmpresaPorEstadoPorFechas(empresaSelected, COMPROBANTE_ESTADO).size());
        }
        int excedente = new BigDecimal(totalComprobantes).subtract(empresaSelected.getPlan().getValorFacturaPlan()).intValue(); 
        pago = pagoService.generarPago(empresaSelected, fechaGeneracion);
        pago.setExcedenteComprobantesPago(excedente);
        pago.setCostoFactPlanPago(empresaSelected.getPlan().getValorFacturaPlan());
        pago.setCostoPlanPago(empresaSelected.getPlan().getCostoPlan());
        pago.setUsuario(loginAccessBean.getUsuarioLogin());
        pago.setFechaRegistroPago(new Date());
        pago.setNumeroComprobantesPago(totalComprobantes);
        pago.setPlan(empresaSelected.getPlan());
        pago.setTotalPago(new BigDecimal(getValorAPagar())); 
        if (pagoService.obtenerPagosPorEmpresaPorMes(empresaSelected, pago.getMesPago()).isEmpty()) {
            getComprobanteList();
            infoMessages(recurso.getString("pago.header"), recurso.getString("pago.detalles.exito"), "form:growl");
            setVisible(true);
        }else{
            warnMessages(recurso.getString("pago.header"), recurso.getString("pago.detalles.generado"), "form:growl");
            setVisible(false);   
        }
    }
    
    public void guardarPago(){
        try {
            pagoService.guardarPago(pago);
            infoMessages(recurso.getString("pago.header"), recurso.getString("pago.detalles.guardado.exito"), "form:growl");
            this.empresaSelected = null;
            this.fechaGeneracion = null;
            this.setVisible(false);
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("pago.header"), ex.getMessage(), "form:growl");
        }
    }
    
    private int obtenerComprobanteList(){
        int result = 0;
        try {      
            comprobantes = pagoService.obtenerLosComprobantesPorEmpresaPorEstadoPorFechas(pago.getEmpresa(), COMPROBANTE_ESTADO);
            if(comprobantes != null){
                result = comprobantes.size();
            }
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("pago.header"), ex.getMessage(), "form:growl");            
        }
        return result;
    }

    private void getComprobanteList() {
        try {
            setTotalComprobantes(obtenerComprobanteList());
            comprobanteModel = new ComprobanteDataTableModel(comprobantes);
            tiposConmprobante = pagoService.obtenerTiposComprobante();
            tipoComprobanteModel = new TipoComprobanteDataTableModel(tiposConmprobante);
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("pago.header"), ex.getMessage(), "form:growl");
        }
    }

    public List<Comprobante> getComprobantesPorTipo(final TipoComprobante tipo) {
        List<Comprobante> result = Collections.emptyList();
        try {
            result = pagoService.obtenerLosComprobantesPorTipo(pago.getEmpresa(), COMPROBANTE_ESTADO, tipo);
        } catch (ServicesException ex) {
            ex.getMessage();
        }
        return result;
    }

    public int getCantidadComprobantesPorTipo(final TipoComprobante tipo) {
        int result = 0;
        try {
            result = pagoService.obtenerLosComprobantesPorTipo(pago.getEmpresa(), COMPROBANTE_ESTADO, tipo).size();
        } catch (ServicesException ex) {
            errorMessages(recurso.getString("pago.header"), ex.getMessage(), "form:growl");
        }
        return result;
    }

    public int getExedente() {
        int result;
        result = new BigDecimal(totalComprobantes).subtract(empresaSelected.getPlan().getValorFacturaPlan()).intValue();
        return result > 0 ? result : result * -1;
    }

    public double getValorAPagar() {
        double result;
        if (totalComprobantes - empresaSelected.getPlan().getValorFacturaPlan().intValue() > 0) {
            result = (empresaSelected.getPlan().getValorFacturaPlan().intValue() + getExedente()) * empresaSelected.getPlan().getCostoPlan().doubleValue();
        } else {
            result = empresaSelected.getPlan().getValorFacturaPlan().intValue();
        }
        return result;
    }

    public Empresa getEmpresaSelected() {
        return empresaSelected;
    }

    public void setEmpresaSelected(Empresa empresaSelected) {
        this.empresaSelected = empresaSelected;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ComprobanteDataTableModel getComprobanteModel() {
        return comprobanteModel;
    }

    public void setComprobanteModel(ComprobanteDataTableModel comprobanteModel) {
        this.comprobanteModel = comprobanteModel;
    }

    public Comprobante getComprobanteSelected() {
        return comprobanteSelected;
    }

    public void setComprobanteSelected(Comprobante comprobanteSelected) {
        this.comprobanteSelected = comprobanteSelected;
    }

    public List<Comprobante> getComprobanteFiltered() {
        return comprobanteFiltered;
    }

    public void setComprobanteFiltered(List<Comprobante> comprobanteFiltered) {
        this.comprobanteFiltered = comprobanteFiltered;
    }

    public List<Comprobante> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(List<Comprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }

    public TipoComprobanteDataTableModel getTipoComprobanteModel() {
        return tipoComprobanteModel;
    }

    public void setTipoComprobanteModel(TipoComprobanteDataTableModel tipoComprobanteModel) {
        this.tipoComprobanteModel = tipoComprobanteModel;
    }

    public List<TipoComprobante> getTiposConmprobante() {
        return tiposConmprobante;
    }

    public void setTiposConmprobante(List<TipoComprobante> tiposConmprobante) {
        this.tiposConmprobante = tiposConmprobante;
    }

    public TipoComprobante getTipoComprobanteSelected() {
        return tipoComprobanteSelected;
    }

    public void setTipoComprobanteSelected(TipoComprobante tipoComprobanteSelected) {
        this.tipoComprobanteSelected = tipoComprobanteSelected;
    }

    public List<TipoComprobante> getTiposComprobanteFiltered() {
        return tiposComprobanteFiltered;
    }

    public void setTiposComprobanteFiltered(List<TipoComprobante> tiposComprobanteFiltered) {
        this.tiposComprobanteFiltered = tiposComprobanteFiltered;
    }

    public int getTotalComprobantes() {
        return totalComprobantes;
    }

    public void setTotalComprobantes(int totalComprobantes) {
        this.totalComprobantes = totalComprobantes;
    }

    public void setLoginAccessBean(LoginAccessBean loginAccessBean) {
        this.loginAccessBean = loginAccessBean;
    }

}

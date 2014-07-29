/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.vo;

import ec.facturaelectronica.datatable.model.ComprobanteDataTableModel;
import ec.facturaelectronica.model.Comprobante;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Christian
 */
public class ComprobanteVo {
    
    private List<Comprobante> listComprobantes;
    private ComprobanteDataTableModel modelComprobante;
    private Comprobante selectComprobante;
    private Date desde;
    private Date hasta;

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }
    
    

    public List<Comprobante> getListComprobantes() {
        return listComprobantes;
    }

    public void setListComprobantes(List<Comprobante> listComprobantes) {
        this.listComprobantes = listComprobantes;
    }

    public ComprobanteDataTableModel getModelComprobante() {
        return modelComprobante;
    }

    public void setModelComprobante(ComprobanteDataTableModel modelComprobante) {
        this.modelComprobante = modelComprobante;
    }

    public Comprobante getSelectComprobante() {
        return selectComprobante;
    }

    public void setSelectComprobante(Comprobante selectComprobante) {
        this.selectComprobante = selectComprobante;
    }
    
    
}

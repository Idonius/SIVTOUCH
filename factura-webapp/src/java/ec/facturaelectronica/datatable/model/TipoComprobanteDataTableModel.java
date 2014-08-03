/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.datatable.model;

import ec.facturaelectronica.model.TipoComprobante;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Armando
 */
public class TipoComprobanteDataTableModel  extends ListDataModel<TipoComprobante> implements SelectableDataModel<TipoComprobante> {

    public TipoComprobanteDataTableModel(List<TipoComprobante> data) {
        super(data);
    }

    @Override
    public Object getRowKey(TipoComprobante tipoComprobante) {
        return tipoComprobante.getId().toString();
    }

    @Override
    public TipoComprobante getRowData(String rowKey) {
        List<TipoComprobante> tiposComprobante = (List<TipoComprobante>) getWrappedData();
        
        for (TipoComprobante tipoComprobante : tiposComprobante) {
            if(tipoComprobante.getId().toString().equals(rowKey)){
                return tipoComprobante;
            }
        }
        return null;
    }
    
}

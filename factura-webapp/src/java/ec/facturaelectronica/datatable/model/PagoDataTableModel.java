/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.datatable.model;

import ec.facturaelectronica.model.Pago;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author armando
 */
public class PagoDataTableModel extends ListDataModel<Pago> implements SelectableDataModel<Pago> {

    public PagoDataTableModel(List<Pago> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Pago pago) {
        return pago.getIdPago().toString();
    }

    @Override
    public Pago getRowData(String rowKey) {
        List<Pago> pagos = (List<Pago>)getWrappedData();
        
        for (Pago pago : pagos) {
            if(pago.getIdPago().toString().equals(rowKey)){
                return pago;
            }
        }
        return null;
    }
    
}

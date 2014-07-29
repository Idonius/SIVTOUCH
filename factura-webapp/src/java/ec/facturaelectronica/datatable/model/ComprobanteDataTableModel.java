/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.datatable.model;

import ec.facturaelectronica.model.Comprobante;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Christian
 */
public class ComprobanteDataTableModel extends ListDataModel<Comprobante> implements SelectableDataModel<Comprobante> {

    public ComprobanteDataTableModel() {
    }

    public ComprobanteDataTableModel(List<Comprobante> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Comprobante obj) {
        return obj.getIdComprobante().toString();
    }

    @Override
    public Comprobante getRowData(String rowKey) {
        List<Comprobante> objs = (List<Comprobante>) getWrappedData();

        for (Comprobante obj : objs) {
            if (obj.getIdComprobante().toString().equals(rowKey)) {
                return obj;
            }
        }

        return null;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.datatable.model;


import ec.facturaelectronica.model.Empresa;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author desarrollotic
 */
public class EmpresaDataTableModel extends ListDataModel<Empresa> implements SelectableDataModel<Empresa> {
   public EmpresaDataTableModel() {
    }

    public EmpresaDataTableModel(List<Empresa> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Empresa obj) {
        return obj.getIdEmpresa().toString();
    }

    @Override
    public Empresa getRowData(String rowKey) {
        List<Empresa> objs = (List<Empresa>) getWrappedData();

        for (Empresa obj : objs) {
            if (obj.getIdEmpresa().toString().equals(rowKey)) {
                return obj;
            }
        }

        return null;
    }  
}

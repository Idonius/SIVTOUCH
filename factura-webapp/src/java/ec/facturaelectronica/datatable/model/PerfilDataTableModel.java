/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.datatable.model;


import ec.facturaelectronica.model.Perfil;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author desarrollotic
 */
public class PerfilDataTableModel extends ListDataModel<Perfil> implements SelectableDataModel<Perfil> {

    public PerfilDataTableModel() {
    }

    public PerfilDataTableModel(List<Perfil> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Perfil perfil) {
        return perfil.getIdPerfil().toString();
    }

    @Override
    public Perfil getRowData(String rowKey) {
        List<Perfil> perfiles = (List<Perfil>) getWrappedData();

        for (Perfil perfil : perfiles) {
            if (perfil.getIdPerfil().toString().equals(rowKey)) {
                return perfil;
            }
        }

        return null;
    }
}

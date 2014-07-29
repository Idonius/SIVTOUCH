/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.datatable.model;


import ec.facturaelectronica.model.Usuario;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author desarrollotic
 */
public class UsuarioDataTableModel extends ListDataModel<Usuario> implements SelectableDataModel<Usuario> {
     public UsuarioDataTableModel() {
    }

    public UsuarioDataTableModel(List<Usuario> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Usuario usuario) {
        return usuario.getIdUsuario().toString();
    }

    @Override
    public Usuario getRowData(String rowKey) {
        List<Usuario> usuarios = (List<Usuario>) getWrappedData();

        for (Usuario usuario : usuarios) {
            if (usuario.getIdUsuario().toString().equals(rowKey)) {
                return usuario;
            }
        }

        return null;
    }  
}

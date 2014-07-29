/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.datatable.model;


import ec.facturaelectronica.model.Certificado;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author desarrollotic
 */
public class CertificadoDataTableModel extends ListDataModel<Certificado> implements SelectableDataModel<Certificado> {
     public CertificadoDataTableModel() {
    }

    public CertificadoDataTableModel(List<Certificado> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Certificado certificado) {
        return certificado.getId().toString();
    }

    @Override
    public Certificado getRowData(String rowKey) {
        List<Certificado> certificados = (List<Certificado>) getWrappedData();

        for (Certificado certificado : certificados) {
            if (certificado.getId().toString().equals(rowKey)) {
                return certificado;
            }
        }

        return null;
    }  
}

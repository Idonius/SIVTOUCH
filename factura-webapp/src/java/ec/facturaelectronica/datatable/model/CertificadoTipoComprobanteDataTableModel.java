/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.datatable.model;

import ec.facturaelectronica.model.CertificadoTipoComprobante;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Armando
 */
public class CertificadoTipoComprobanteDataTableModel extends ListDataModel<CertificadoTipoComprobante> implements SelectableDataModel<CertificadoTipoComprobante>{

    public CertificadoTipoComprobanteDataTableModel(List<CertificadoTipoComprobante> data) {
        super(data);        
    }

    @Override
    public Object getRowKey(CertificadoTipoComprobante certificadoTipoComprobante) {
        return certificadoTipoComprobante.getId().toString();
    }

    @Override
    public CertificadoTipoComprobante getRowData(String rowKey) {
        CertificadoTipoComprobante result = new CertificadoTipoComprobante();
        
        List<CertificadoTipoComprobante> certificadoTipoComprobanteList = (List<CertificadoTipoComprobante>)getWrappedData();
        for (CertificadoTipoComprobante certificadoTipoComprobante : certificadoTipoComprobanteList) {
            if(certificadoTipoComprobante.getId().equals(rowKey)){
                result = certificadoTipoComprobante;
            }
        }
        return result;
    }
}

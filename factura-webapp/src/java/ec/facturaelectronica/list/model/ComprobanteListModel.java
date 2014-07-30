/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.list.model;

import ec.facturaelectronica.bean.application.ComprobanteApplication;
import ec.facturaelectronica.model.Comprobante;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Christian
 */
@FacesConverter(value = "comprobanteListModel")
public class ComprobanteListModel implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        
        if (value.trim().equals("")) {
            return null;
        } else {
            ComprobanteApplication service = (ComprobanteApplication) fc.getExternalContext().getApplicationMap().get("comprobanteApplication");

            for (Comprobante obj : service.getListComprobantes()) {
                if (obj.getIdComprobante().toString().equals(value)) {
                    return obj;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Comprobante) object).getIdComprobante());
        } else {
            return null;
        }
    }
}

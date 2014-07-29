/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.list.model;

import ec.facturaelectronica.model.TipoComprobante;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Armando
 */
@FacesConverter(forClass = TipoComprobante.class, value = "TipoComprobanteListModel")
public class TipoComprobanteListModel implements Converter{
    
    public static List<TipoComprobante> tipoComprobanteModel;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        System.err.println("valor:"+submittedValue);
        if (submittedValue.trim().equals("")) {
            return null;
        } else {

            for (TipoComprobante obj : tipoComprobanteModel) {
                if (obj.getId().toString().equals(submittedValue)) {
                    System.err.println(obj);
                    return obj;
                }
            }
        }

        return null;        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((TipoComprobante) value).getId());
        }        
    }
    
}

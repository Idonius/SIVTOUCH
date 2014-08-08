/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.list.model;

import ec.facturaelectronica.model.Catalogo;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author armando
 */
@FacesConverter(forClass = Catalogo.class, value = "TipoTransaccionListModel")
public class TipoTransaccionListModel implements Converter {
    
    public static List<Catalogo> catalogoModel;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {

            for (Catalogo obj : catalogoModel) {
                if (obj.getIdEmpresa().toString().equals(submittedValue)) {
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
            return String.valueOf(((Catalogo) value).getIdCatalogo());
        }        
    }
    
}

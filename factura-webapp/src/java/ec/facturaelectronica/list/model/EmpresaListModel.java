/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.list.model;


import ec.facturaelectronica.model.Empresa;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author desarrollotic
 */
@FacesConverter(forClass = Empresa.class, value = "EmpresaListModel")
public class EmpresaListModel implements Converter {

    public static List<Empresa> empresaModel;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {

            for (Empresa obj : empresaModel) {
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
            return String.valueOf(((Empresa) value).getIdEmpresa());
        }
    }
}

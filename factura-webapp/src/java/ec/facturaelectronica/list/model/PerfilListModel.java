/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.list.model;



import ec.facturaelectronica.model.Perfil;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author desarrollotic
 */
@FacesConverter(forClass = Perfil.class, value = "PerfilListModel")
public class PerfilListModel implements Converter{
    
    public static List<Perfil> perfilModel;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {

            for (Perfil perfil : perfilModel) {
                if (perfil.getIdPerfil().toString().equals(submittedValue)) {
                    return perfil;
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
            return String.valueOf(((Perfil) value).getIdPerfil());
        }
    }
}

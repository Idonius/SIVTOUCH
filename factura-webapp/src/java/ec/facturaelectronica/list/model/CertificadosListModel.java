/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.list.model;

import ec.facturaelectronica.model.Certificado;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Christian
 */
@FacesConverter(forClass = Certificado.class, value = "CertificadosListModel")
public class CertificadosListModel implements Converter {
    
    public static List<Certificado> certificadoModel;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {

            for (Certificado obj : certificadoModel) {
                if (obj.getIdCertificado().toString().equals(submittedValue)) {
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
            return String.valueOf(((Certificado) value).getIdCertificado());
        }
    }
}

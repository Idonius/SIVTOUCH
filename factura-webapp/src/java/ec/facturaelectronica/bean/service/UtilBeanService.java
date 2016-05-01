/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean.service;

import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Perfil;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

/**
 *
 * @author Christian
 */
@ManagedBean(name = "utilBeanService", eager = true)
@ApplicationScoped
public class UtilBeanService {

    public List<SelectItem> getItemsPerfiles(List<Perfil> perfiles) {
        List<SelectItem> items = new ArrayList<>();

        for (Perfil perfil : perfiles) {
            SelectItem item = new SelectItem(perfil.getIdPerfil(), perfil.getNombrePerfil());
            items.add(item);
        }

        return items;
    }
    
     public List<SelectItem> getItemsEmpresas(List<Empresa> empresas) {
        List<SelectItem> items = new ArrayList<>();

        for (Empresa empresa : empresas) {
            SelectItem item = new SelectItem(empresa.getIdEmpresa(), empresa.getNombreEmpresa());
            items.add(item);
        }

        return items;
    }
}

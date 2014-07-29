/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.bean.application;


import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.service.FacturaService;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;


/**
 *
 * @author Christian
 */
@ManagedBean(name = "comprobanteApplication")
@ApplicationScoped
public class ComprobanteApplication {

    @EJB
    FacturaService facturaService;
    
    private List<Comprobante> listComprobantes;
    
    
    public ComprobanteApplication() {
    }
    public void init(Empresa empresa){
        try {
            listComprobantes=facturaService.getComprobantesByEmpresa(empresa, new Date(), new Date());
        } catch (ServicesException ex) {
            Logger.getLogger(ComprobanteApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    /**
     * @return the listComprobantes
     */
    public List<Comprobante> getListComprobantes() {
        return listComprobantes;
    }

    /**
     * @param listComprobantes the listComprobantes to set
     */
    public void setListComprobantes(List<Comprobante> listComprobantes) {
        this.listComprobantes = listComprobantes;
    }

  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.ComprobanteDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.service.FacturaService;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Christian
 */
@Stateless
public class FacturaServiceImpl implements FacturaService{

    @EJB
    ComprobanteDao comprobanteDao;
    
    @Override
    public List<Comprobante> getComprobantesByEmpresa(Empresa empresa, Date desde, Date hasta) throws ServicesException {
        
        return comprobanteDao.getComprobantes(empresa, desde, hasta);
        
    }
    
}

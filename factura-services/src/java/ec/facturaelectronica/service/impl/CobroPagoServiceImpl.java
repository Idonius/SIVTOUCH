/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.CobroPagoDao;
import ec.facturaelectronica.dao.PagoDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.service.CobroPagoService;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class CobroPagoServiceImpl implements CobroPagoService{
    @EJB
    private CobroPagoDao cobroPagoDao;
    
    @EJB
    private PagoDao pagoDao;
    
    @EJB
    private CatalogoDao catalogoDao;

    @Override
    public List<Pago> obtenerPagosGenerados() throws ServicesException {
        List<Pago> result = Collections.emptyList();
        
        try{
            result = pagoDao.obtenerPagosGenerados();
        }catch(Exception ex){
            throw new ServicesException("Error al obtener listado de pagos..");
        }
        return result;
    }

    @Override
    public List<Catalogo> obtenerCatalogoTrans() throws ServicesException {
        List<Catalogo> result = Collections.emptyList();
        
        try{
            result = catalogoDao.getCatalogoByGrupoTrans();
        }catch(Exception ex){
            throw new ServicesException("Error al obtener lista de catalogos...");
        }
        return result;
    }
    
    
}

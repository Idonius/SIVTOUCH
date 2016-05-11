/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.ComprobanteDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.service.ComprobanteService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class ComprobanteServiceImpl implements ComprobanteService{

    @EJB
    private ComprobanteDao comprobanteDao;
    
    @Override
    public List<Comprobante> obtenerComprobantePorIdentificadorComprobanteList(final String identificador) throws ServicesException {
        try{
            return comprobanteDao.obtenerComprobantePorIdentificadorComprobante(identificador);
        }catch(Exception ex){
            throw new ServicesException("Error al obtener los registros...");
        }
    }
    
}

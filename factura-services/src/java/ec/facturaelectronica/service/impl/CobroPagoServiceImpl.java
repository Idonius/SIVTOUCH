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
import ec.facturaelectronica.model.CobroPago;
import ec.facturaelectronica.model.GrupoCatalogo;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.service.CobroPagoService;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author armando
 */
@Stateless
public class CobroPagoServiceImpl implements CobroPagoService {

    @EJB
    private CobroPagoDao cobroPagoDao;

    @EJB
    private PagoDao pagoDao;
    
    @EJB
    private CatalogoDao catalogoDao;

    @Override
    public List<Pago> obtenerPagosGeneradosService() throws ServicesException {
        try {
            return pagoDao.obtenerPagosGenerados();
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener los pagos generados...");
        }
    }

    @Override
    public void guardarCobroPago(final CobroPago cobroPago) throws ServicesException {
        try {
            if (cobroPago != null) {
                cobroPagoDao.insert(cobroPago);
            }
        } catch (Exception ex) {
            throw new ServicesException("Error al guardar aprobacion de pagos...");
        }
    }

    @Override
    public List<Catalogo> obtenerCatalogosPorGrupoTransacciones() throws ServicesException {
        List<Catalogo> result = Collections.emptyList();
        GrupoCatalogo grupoCatalogo = new GrupoCatalogo(3L);
        try{
            result = catalogoDao.getCatalogoByGrupo(grupoCatalogo);
        }catch(Exception ex){
            throw new ServicesException("Error. La lista catalogos no pudo ser obtenida");
        }
        return result;
    }
    
}

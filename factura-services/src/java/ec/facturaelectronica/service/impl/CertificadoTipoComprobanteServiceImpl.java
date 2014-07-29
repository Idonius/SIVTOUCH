/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.CertificadoTipoComprobanteDao;
import ec.facturaelectronica.dao.TipoComprobanteDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.CertificadoTipoComprobanteService;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class CertificadoTipoComprobanteServiceImpl implements CertificadoTipoComprobanteService {

    @EJB
    private CertificadoTipoComprobanteDao certificadoTipoComprobanteDao;
    
    @EJB
    private CatalogoDao catalogoDao;
    
    @EJB
    private TipoComprobanteDao tipoComprobanteDao;
    
    @Override
    public List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobanteServiceList() throws ServicesException {
        List<CertificadoTipoComprobante> result = Collections.emptyList();
        try{
            result = certificadoTipoComprobanteDao.obtenerCertificadoTipoComprobanteList();
        }catch(Exception ex){
            throw new ServicesException("Error al intentar obtener el listado de certificados por comprobantes...");
        }
        return result;
    }

    @Override
    public void agregarCertificadoTipoComprobanteService(final CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException {
        try{
            certificadoTipoComprobanteDao.insert(certificadoTipoComprobante);
        }catch(Exception ex){
            throw new ServicesException("Error al intentar registrar detalles certificados por comprobantes...");
        }
    }

    @Override
    public void actualizarCertificadoTipoComprobanteService(CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException {
        try{
            certificadoTipoComprobanteDao.update(certificadoTipoComprobante);
        }catch(Exception ex){
            throw new ServicesException("Error al intentar modificar registro detalles certificados por comprobantes...");
        }
    }

    @Override
    public void eliminarCertificadoTipoComprobanteService(final CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException {
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Desactivado.getOrden());
            certificadoTipoComprobante.setCatalogo(catalogo);
            certificadoTipoComprobanteDao.update(certificadoTipoComprobante);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
        
    }

    @Override
    public List<TipoComprobante> obtenerTipoComprobanteList() throws ServicesException{
        List<TipoComprobante> result = Collections.emptyList();
        
        try{
            result = tipoComprobanteDao.obtenerTipoComprobanteList();
            System.err.println("Cantidad " + result);
        }catch(Exception ex){
            throw new ServicesException(ex.getMessage());
        }
        return result;
    }
        
}

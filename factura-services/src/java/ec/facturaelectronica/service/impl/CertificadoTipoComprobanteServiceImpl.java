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
import ec.facturaelectronica.model.Empresa;
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
    public List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobanteServiceList(Empresa empresa) throws ServicesException {
        List<CertificadoTipoComprobante> result;
        Catalogo catalogo;

        try {

            catalogo = catalogoDao.load(EstadosGeneralesEnum.Activo.getOrden());
            result = certificadoTipoComprobanteDao.obtenerCertificadoTipoComprobanteList(catalogo, empresa);
        } catch (Exception ex) {
            throw new ServicesException("Error al intentar obtener el listado de certificados por comprobantes...", ex);
        }
        return result;
    }

    @Override
    public void agregarCertificadoTipoComprobanteService(final CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException {
        try {
            certificadoTipoComprobanteDao.insert(certificadoTipoComprobante);
        } catch (Exception ex) {
            throw new ServicesException("Error al intentar registrar detalles certificados por comprobantes...");
        }
    }

    @Override
    public void actualizarCertificadoTipoComprobanteService(CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException {
        try {
            certificadoTipoComprobanteDao.update(certificadoTipoComprobante);
        } catch (Exception ex) {
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
    public List<TipoComprobante> obtenerTipoComprobanteList() throws ServicesException {
        List<TipoComprobante> result = Collections.emptyList();

        try {
            result = tipoComprobanteDao.obtenerTipoComprobanteList();
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
        return result;
    }

    @Override
    public List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobante(final Empresa empresa, final TipoComprobante tipoComprobante) throws ServicesException {
        List<CertificadoTipoComprobante> result;
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load(EstadosGeneralesEnum.Activo.getOrden());

            result = certificadoTipoComprobanteDao.obtenerCertificadoPorEmpresaYTipoComprobante(catalogo, empresa, tipoComprobante);
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el certificado", ex);
        }
        return result;
    }

    @Override
    public CertificadoTipoComprobante buscarCertificado(Empresa empresa, TipoComprobante tipoComprobante) throws ServicesException {
        CertificadoTipoComprobante result;
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load(EstadosGeneralesEnum.Activo.getOrden());

            result = certificadoTipoComprobanteDao.obtenerCertificado(catalogo, empresa, tipoComprobante);
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el certificado", ex);
        }
        return result;
    }

}

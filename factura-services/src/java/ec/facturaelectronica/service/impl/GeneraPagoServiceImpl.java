/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.dao.ComprobanteDao;
import ec.facturaelectronica.dao.TipoComprobanteDao;
import ec.facturaelectronica.dao.PagoDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.DetallePago;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.GrupoCatalogo;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.TipoComprobantePago;
import ec.facturaelectronica.service.GeneraPagoService;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;

/**
 *
 * @author Armando
 */
enum tipoFecha {

    INICIO(0), FIN(1);

    private int value;

    private tipoFecha(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

@Stateless
public class GeneraPagoServiceImpl implements GeneraPagoService {
    private final Long GRUPO_CATALOGO_PAGO = 2L;
    private final String ESTADO_CATALOGO_GENERADO = "Generado";

    @EJB
    private EmpresaDao empresaDao;

    @EJB
    private ComprobanteDao comprobanteDao;

    @EJB
    private TipoComprobanteDao tipoComprobanteDao;

    @EJB
    private PagoDao pagoDao;
    
    @EJB
    private CatalogoDao catalogoDao;

    @Override
    public Pago generarPago(Empresa empresa, Date fecha) throws ServicesException {
        GrupoCatalogo grupoCatalogo = new GrupoCatalogo(GRUPO_CATALOGO_PAGO);
        List<Catalogo> catalogos;
                
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        Pago pago = new Pago();
        //generar las fechas desde hasta

        pago.setFechaDesdePago(obtenerRangosFechaPrevia(tipoFecha.INICIO));
        pago.setFechaHastaPago(obtenerRangosFechaPrevia(tipoFecha.FIN));
        pago.setAnioPago(calendar.get(Calendar.YEAR));
        pago.setMesPago(calendar.get(Calendar.MONTH) + 1);
        pago.setEmpresa(empresa);
        pago.setFechaGeneracionPago(fecha);
        catalogos = catalogoDao.getCatalogoByGrupoByNombre(grupoCatalogo, ESTADO_CATALOGO_GENERADO);
        pago.setEstado(catalogos.get(0));
        pago.setCostoFactPlanPago(BigDecimal.ZERO);
        return pago;

    }

    @Override
    public void guardarPago(Pago pago) throws ServicesException {
        List<Comprobante> comprobantes = obtenerLosComprobantesPorEmpresaPorEstadoPorFechas(pago.getEmpresa(), "AUTORIZADO");
        List<TipoComprobante> tiposComprobante = tipoComprobanteDao.obtenerTipoComprobanteList();
        List<TipoComprobantePago> tipoComprobantePagoList = new ArrayList<TipoComprobantePago>();
        List<DetallePago> detallePagoList = new ArrayList<DetallePago>();
    
        for (Comprobante comprobante : comprobantes) {
            DetallePago detallePago = new DetallePago();
            detallePago.setComprobante(comprobante);
            detallePago.setIdPago(pago);
            detallePagoList.add(detallePago);
        }
        pago.setDetallePagoList(detallePagoList);
        
        for (TipoComprobante tipoComprobante : tiposComprobante) {
            List<Comprobante> comprobantesPorTipo = obtenerLosComprobantesPorTipo(pago.getEmpresa(), "AUTORIZADO", tipoComprobante);
            TipoComprobantePago tipoComprobantePago = new TipoComprobantePago();
            int contador = 0;
            for (Comprobante comprobante : comprobantesPorTipo) {
                contador++;
                tipoComprobantePago.setNumeroComprobantesTipo(contador);
            }
            tipoComprobantePago.setIdPago(pago);
            tipoComprobantePago.setTipoComprobante(tipoComprobante);
            tipoComprobantePagoList.add(tipoComprobantePago);
        }
        pago.setTipoComprobantePagoList(tipoComprobantePagoList);
        pagoDao.insert(pago);
    }
    
    @Override
    public void actualizarPago(final Pago pago){
        if(pago != null){
            pagoDao.update(pago);
        }
    }

    private Date obtenerRangosFechaPrevia(final tipoFecha valor) {
        Date hoy = new Date();
        Calendar fecha = new GregorianCalendar();

        fecha.setTime(hoy);
        fecha.add(Calendar.MONTH, -1);

        switch (valor) {
            case INICIO:
                fecha.set(Calendar.DAY_OF_MONTH, fecha.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case FIN:
                fecha.set(Calendar.DAY_OF_MONTH, fecha.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return fecha.getTime();
    }

    @Override
    public List<Empresa> obtenerTodasLasEmpresasActivas() throws ServicesException {
        List<Empresa> result = Collections.emptyList();
        try {
            result = empresaDao.getEmpresas();
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener listado de empresas");
        }
        return result;
    }

    @Override
    public List<Comprobante> obtenerLosComprobantesPorEmpresaPorEstadoPorFechas(Empresa empresa, String estado) throws ServicesException {
        List<Comprobante> result = Collections.emptyList();
        try {
            result = comprobanteDao.obtenerComprobantesPorEmpresaPorEstadoPorFechas(empresa, estado, obtenerRangosFechaPrevia(tipoFecha.INICIO), obtenerRangosFechaPrevia(tipoFecha.FIN));
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el listado de comprobantes...");
        }
        return result;
    }

    @Override
    public List<TipoComprobante> obtenerTiposComprobante() throws ServicesException {
        List<TipoComprobante> result = Collections.emptyList();
        try {
            result = tipoComprobanteDao.obtenerTipoComprobanteList();
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el listado de tipos de comprobantes...");
        }
        return result;
    }

    @Override
    public List<Comprobante> obtenerLosComprobantesPorTipo(Empresa empresa, String estado, TipoComprobante tipo) throws ServicesException {
        List<Comprobante> result = Collections.emptyList();
        try {
            result = comprobanteDao.obtenerComprobantesPorEmpresaPorEstadoPorFechasPorTipo(empresa, estado, obtenerRangosFechaPrevia(tipoFecha.INICIO), obtenerRangosFechaPrevia(tipoFecha.FIN), tipo);
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el listado de comprobantes...");
        }
        return result;
    }

    @Override
    public List<Pago> obtenerPagosPorEmpresaPorMes(Empresa empresa, int mes) throws ServicesException {
        List<Pago> result = Collections.emptyList();

        try {
            result = pagoDao.obtenerPagosPorEmpresaPorMes(empresa, mes);
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el listado de pagos...");
        }
        return result;
    }

    @Override
    public List<Catalogo> obtenerCatalogoPorGrupoCatalogoPorNombreCatalogo(final GrupoCatalogo grupo, final String nombreCatalogo) throws ServicesException {
        List<Catalogo> result = Collections.emptyList();
        try {
            result = catalogoDao.getCatalogoByGrupoByNombre(grupo, nombreCatalogo);
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el listado de catalogos..");
        }
        return result;
    }

}

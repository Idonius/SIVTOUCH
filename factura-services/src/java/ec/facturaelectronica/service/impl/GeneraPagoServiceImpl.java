/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.dao.ComprobanteDao;
import ec.facturaelectronica.dao.TipoComprobanteDao;
import ec.facturaelectronica.dao.PagoDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.service.GeneraPagoService;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;

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

    @EJB
    private EmpresaDao empresaDao;

    @EJB
    private ComprobanteDao comprobanteDao;

    @EJB
    private TipoComprobanteDao tipoComprobanteDao;
    
    @EJB
    private PagoDao pagoDao;

    private final String FORMATO_FECHA = "dd-MM-yyyy";

    private int getPreviousMonth(final int mes) {
        return mes == 1 ? 12 : mes - 1;
    }

    @Override
    public Pago generarPago(Empresa empresa, Date fecha) throws ServicesException {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        System.out.println("Fecha: " + new SimpleDateFormat("dd-MMM-yyyy").format(fecha));
        Pago pago = new Pago();
        //generar las fechas desde hasta

        pago.setFechaDesdePago(obtenerRangosFechaPrevia(tipoFecha.INICIO));
        pago.setFechaHastaPago(obtenerRangosFechaPrevia(tipoFecha.FIN));
        pago.setAnioPago(calendar.get(Calendar.YEAR));
        pago.setMesPago(calendar.get(Calendar.MONTH) + 1);
        pago.setEmpresa(empresa);
        pago.setFechaGeneracionPago(fecha);

        //validar que no existe pagos para esa empresa en esa fchas
        //consulta los comprobnates de esa empresa que esten autorizados por las fechas desde y hasta
        //foreach
//        DetallePago detaPago = new DetallePago();
//        pago.getDetallePagoList().add(detaPago);
        //fin for each
        //consulta de los tipos compronates fact,notas de venta,retenciones
        //for each de tipos coprobante
        //conulta de comprobnates de la empresa que esten autorizados por as fechas desde hasta y el tipo de compronate
//        TipoComprobantePago tipoComprobantePago = new TipoComprobantePago();
//        pago.getTipoComprobantePagoList().add(tipoComprobantePago);
        //end for
        return pago;

    }

    @Override
    public void guardarPago(Pago pago) throws ServicesException {
        
        //persitencia pagoDao.insert(pago);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            System.out.println("Cantidad comprobantes: " + result.size());
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
            System.out.println("Cantidad comprobantes: " + result.size());
        } catch (Exception ex) {
            throw new ServicesException("Error al obtener el listado de comprobantes...");
        }
        return result;
    }

    @Override
    public List<Pago> obtenerPagosPorEmpresaPorMes(Empresa empresa, int mes) throws ServicesException {
        List<Pago> result = Collections.emptyList();
        
        try{
            result = pagoDao.obtenerPagosPorEmpresaPorMes(empresa, mes);
        }catch(Exception ex){
            throw new ServicesException("Error al obtener el listado de pagos...");
        }       
        return result;
    }
    
    

}

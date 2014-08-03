/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Pago;
import ec.facturaelectronica.model.TipoComprobante;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface GeneraPagoService {
    public Pago generarPago(Empresa empresa, Date fecha) throws ServicesException;
    public void guardarPago(Pago pago) throws ServicesException;
    List<Empresa> obtenerTodasLasEmpresasActivas() throws ServicesException;
    List<Comprobante> obtenerLosComprobantesPorEmpresaPorEstadoPorFechas(Empresa empresa, String estado) throws ServicesException;
    List<TipoComprobante> obtenerTiposComprobante() throws ServicesException;
    List<Comprobante> obtenerLosComprobantesPorTipo(Empresa empresa, String estado, TipoComprobante tipo) throws ServicesException;
    List<Pago> obtenerPagosPorEmpresaPorMes(Empresa empresa, int mes) throws ServicesException;
}

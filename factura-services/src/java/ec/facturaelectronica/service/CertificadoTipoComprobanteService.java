/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface CertificadoTipoComprobanteService {
    List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobanteServiceList() throws ServicesException;
    void agregarCertificadoTipoComprobanteService(final CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException;
    void actualizarCertificadoTipoComprobanteService(final CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException;
    void eliminarCertificadoTipoComprobanteService(CertificadoTipoComprobante certificadoTipoComprobante) throws ServicesException;
    List<TipoComprobante> obtenerTipoComprobanteList() throws ServicesException;
    List<TipoComprobante> obtenerCertificadoTipoComprobante(Empresa empresa, TipoComprobante tipoComprobante) throws ServicesException;
}

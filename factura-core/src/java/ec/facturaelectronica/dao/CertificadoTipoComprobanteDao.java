/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.Catalogo;
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
public interface CertificadoTipoComprobanteDao extends GenericDao<CertificadoTipoComprobante, Integer> {

    List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobanteList(Catalogo estado, Empresa empresa);

    List<CertificadoTipoComprobante> obtenerCertificadoPorEmpresaYTipoComprobante(Catalogo estado, Empresa empresa, TipoComprobante tipoComprobante);
}

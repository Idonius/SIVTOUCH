/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service;

import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.Empresa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface CertificadoService {
    List<Certificado> getTodosLosCertificados();
    boolean registrarCertificado(final Certificado certificado);
    boolean actualizarCertificado(final Certificado certificado);
    Catalogo getCatalogoPorId(final Long idCatalogo);
    List<Certificado> getCertificadosFiltrados();
    Empresa getEmpresaPorId(final int idEmpresa);
    Certificado getCertificadoPorId(Long id);
}

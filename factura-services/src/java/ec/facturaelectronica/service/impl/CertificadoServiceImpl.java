/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.CertificadoDao;
import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.service.CertificadoService;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class CertificadoServiceImpl implements CertificadoService{
    
    @EJB
    private CertificadoDao certificadoDao;
    
    @EJB
    private CatalogoDao catalogoDao;
    
    @EJB
    private EmpresaDao empresaDao;
    
    @Override
    public List<Certificado> getTodosLosCertificados() {
        return certificadoDao.obtenerTodosLosCertificados();
    }

    @Override
    public boolean registrarCertificado(final Certificado certificado) {
        boolean result = Boolean.FALSE;
        if(certificado != null){
            certificadoDao.insert(certificado);
            result = Boolean.TRUE;
        }
        return result;
    }

    @Override
    public boolean actualizarCertificado(final Certificado certificado) {
        boolean result = Boolean.FALSE;
        if(certificado != null){
            certificadoDao.update(certificado);
            result = Boolean.TRUE;
        }
        return result;        
    }

    @Override
    public Catalogo getCatalogoPorId(Long idCatalogo) {
        return catalogoDao.load(idCatalogo);
    }

    @Override
    public List<Certificado> getCertificadosFiltrados() {
        List<Certificado> result = Collections.emptyList();
        if(certificadoDao.obtenerCertificadosFiltrados().size() > 0){
            result = certificadoDao.obtenerCertificadosFiltrados();
        }
        return result;
    }

    @Override
    public Empresa getEmpresaPorId(int idEmpresa) {
        return empresaDao.load(idEmpresa);
    }

    @Override
    public Certificado getCertificadoPorId(int id) {
        return certificadoDao.load(id);
    }
    
    
    
    
    
    
    
    
    
    
}

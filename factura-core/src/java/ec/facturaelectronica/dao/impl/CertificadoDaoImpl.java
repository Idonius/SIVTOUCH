/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.CertificadoDao;
import ec.facturaelectronica.model.Certificado;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Armando
 */
@Stateless
public class CertificadoDaoImpl extends GenericDaoImpl<Certificado, Long> implements CertificadoDao{
    private final static Logger LOGGER = Logger.getLogger(CertificadoDaoImpl.class.getName());
    public CertificadoDaoImpl() {
        super(Certificado.class);
    }

    @Override
    public List<Certificado> obtenerTodosLosCertificados() {
        List<Certificado> result;
        
        Query qry = em.createNamedQuery("Certificado.findAll");
        result = qry.getResultList();
        if(result != null){
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Certificado> obtenerCertificadosFiltrados() {
        List<Certificado> result;
        
        Query qry = em.createNamedQuery("Certificado.filtered");
        result = qry.getResultList();
        if(result != null){
            return result;
        }
        return Collections.emptyList();        
    }    
    
    
}

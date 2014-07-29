/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.CertificadoTipoComprobanteDao;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Armando
 */
@Stateless
public class CertificadoTipoComprobanteDaoImpl extends GenericDaoImpl<CertificadoTipoComprobante, Integer> implements CertificadoTipoComprobanteDao, Serializable{

    public CertificadoTipoComprobanteDaoImpl() {
        super(CertificadoTipoComprobante.class);
    }

    @Override
    public List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobanteList() {
        List<CertificadoTipoComprobante>  result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("CertificadoTipoComprobante.findAll");
        if(qry != null){
            result = qry.getResultList();
        }
        return result;
    }
    
}

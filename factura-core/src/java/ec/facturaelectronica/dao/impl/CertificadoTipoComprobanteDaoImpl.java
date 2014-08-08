/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.CertificadoTipoComprobanteDao;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
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
    
    @Override
    public List<TipoComprobante> obtenerCertificadoPorEmpresaYTipoComprobante(Empresa empresa, TipoComprobante tipoComprobante) {
        List<TipoComprobante> result = Collections.emptyList();
        String valorQuery = "SELECT ctc FROM CertificadoTipoComprobante ctc WHERE ctc.certificado.empresa=:empresa AND ctc.tipoComprobante=:tipoComprobante";
        Query qry = em.createQuery(valorQuery);        
        if(qry != null){
            qry.setParameter("empresa", empresa)
               .setParameter("tipoComprobante", tipoComprobante);       
            result = qry.getResultList();
        }
        return result;
    }
    
    
    
}

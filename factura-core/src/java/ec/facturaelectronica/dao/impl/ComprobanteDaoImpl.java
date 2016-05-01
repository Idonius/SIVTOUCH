/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.ComprobanteDao;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Christian
 */
@Stateless
public class ComprobanteDaoImpl extends GenericDaoImpl<Comprobante, Long> implements ComprobanteDao {

    public ComprobanteDaoImpl() {
        super(Comprobante.class);
    }

    @Override
    public List<Comprobante> getComprobantes(Empresa empresa, Date desde, Date hasta) {
        List<Comprobante> lista;

        Query q;

        q = em.createNamedQuery("Comprobante.findByEmpresa");
        q.setParameter("idEmpresa", empresa);
        q.setParameter("fechaDesde", desde);
        q.setParameter("fechaHasta", hasta);
     
        
        
        lista=q.getResultList();
        
        return lista;

    }

    @Override
    public List<Comprobante> obtenerComprobantesPorEmpresaPorEstadoPorFechas(Empresa empresa, String estado, Date fechaInicio, Date fechaFin) {
        List<Comprobante> result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("Comprobante.findByEmpresaByEstadoByFechas");
        if(qry != null){
            qry.setParameter("idEmpresa", empresa)
               .setParameter("estado", estado)
               .setParameter("fechaDesde", fechaInicio)
               .setParameter("fechaHasta", fechaFin);
            result = qry.getResultList();
        }
        return result;
    }

    @Override
    public List<Comprobante> obtenerComprobantesPorEmpresaPorEstadoPorFechasPorTipo(Empresa empresa, String estado, Date fechaInicio, Date fechaFin, TipoComprobante tipo) {
       List<Comprobante> result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("Comprobante.findByTipoComprobante");
        if(qry != null){
            qry.setParameter("idEmpresa", empresa)
               .setParameter("estado", estado)
               .setParameter("fechaDesde", fechaInicio)
               .setParameter("fechaHasta", fechaFin)
               .setParameter("tipo", tipo);
            result = qry.getResultList();
        }
        return result;
        
    }
    
    
}

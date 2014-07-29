/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.ComprobanteDao;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
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
}

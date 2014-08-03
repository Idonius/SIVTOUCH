/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.PagoDao;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Pago;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Armando
 */
@Stateless
public class PagoDaoImpl extends GenericDaoImpl<Pago, Integer> implements PagoDao{

    public PagoDaoImpl() {
        super(Pago.class);
    }

    @Override
    public List<Pago> obtenerTodosLosPagos() {
        List<Pago> result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("Pago.findAllActive");
        
        if(qry != null){
            result = qry.getResultList();
        }
        return result;
    }

    @Override
    public List<Pago> obtenerPagosPorEmpresaPorMes(Empresa empresa, int mes) {
        List<Pago> result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("Pago.findByEmpresaByMes");
        if(qry != null){
            qry.setParameter("emp", empresa)
               .setParameter("mes", mes);
            result = qry.getResultList();
        }
        return result;
    }
    
    
    
}

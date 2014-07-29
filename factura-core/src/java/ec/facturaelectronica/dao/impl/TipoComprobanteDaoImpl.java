/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.TipoComprobanteDao;
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
public class TipoComprobanteDaoImpl extends GenericDaoImpl<TipoComprobante, Integer> implements TipoComprobanteDao, Serializable{

    public TipoComprobanteDaoImpl() {
        super(TipoComprobante.class);
    }

    @Override
    public List<TipoComprobante> obtenerTipoComprobanteList() {
        List<TipoComprobante> result = Collections.emptyList();
        Query qry = em.createNamedQuery("tipoComprobante.findAll");
        if(qry != null){
            result = qry.getResultList();
        }
        return result;
    }
    
}

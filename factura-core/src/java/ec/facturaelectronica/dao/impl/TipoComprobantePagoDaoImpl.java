/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.model.TipoComprobantePago;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class TipoComprobantePagoDaoImpl extends GenericDaoImpl<TipoComprobantePago, Integer>{

    public TipoComprobantePagoDaoImpl() {
        super(TipoComprobantePago.class);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

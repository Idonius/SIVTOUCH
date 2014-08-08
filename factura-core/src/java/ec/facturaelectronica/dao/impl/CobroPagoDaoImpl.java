/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.CobroPagoDao;
import ec.facturaelectronica.model.CobroPago;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class CobroPagoDaoImpl extends GenericDaoImpl<CobroPago, Integer> implements CobroPagoDao{

    public CobroPagoDaoImpl() {
        super(CobroPago.class);
    }
    
}

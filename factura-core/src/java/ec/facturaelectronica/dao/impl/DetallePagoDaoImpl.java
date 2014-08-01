/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.DetallePagoDao;
import ec.facturaelectronica.model.DetallePago;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class DetallePagoDaoImpl extends GenericDaoImpl<DetallePago, Integer> implements DetallePagoDao, Serializable{

    public DetallePagoDaoImpl() {
        super(DetallePago.class);
    }
    
}

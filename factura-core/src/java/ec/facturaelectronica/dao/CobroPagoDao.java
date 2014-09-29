/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.CobroPago;
import javax.ejb.Local;

/**
 *
 * @author Christian
 */
@Local
public interface CobroPagoDao extends GenericDao<CobroPago, Integer>{
}

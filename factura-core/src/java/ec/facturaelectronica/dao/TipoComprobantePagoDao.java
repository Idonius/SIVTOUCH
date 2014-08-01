/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.TipoComprobantePago;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface TipoComprobantePagoDao extends GenericDao<TipoComprobantePago, Integer>{
    
}

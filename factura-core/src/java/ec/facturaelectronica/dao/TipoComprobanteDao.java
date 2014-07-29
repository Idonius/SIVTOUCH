/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.TipoComprobante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface TipoComprobanteDao extends GenericDao<TipoComprobante, Integer>{
    List<TipoComprobante> obtenerTipoComprobanteList();
}

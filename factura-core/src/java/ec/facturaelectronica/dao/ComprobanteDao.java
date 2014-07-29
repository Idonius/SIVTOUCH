/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Christian
 */
@Local
public interface ComprobanteDao extends GenericDao<Comprobante , Long>{
    public List<Comprobante> getComprobantes(Empresa empresa,Date desde,Date hasta);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.Cliente;
import javax.ejb.Local;

/**
 *
 * @author Christian
 */
@Local
public interface ClienteDao extends GenericDao<Cliente, Long>{
    
    public Cliente buscarCliente(String ruc);
    
    
}

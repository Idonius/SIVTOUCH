/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.ClienteDao;
import ec.facturaelectronica.model.Cliente;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Christian
 */
@Stateless
public class ClienteDaoImpl extends GenericDaoImpl<Cliente, Long> implements ClienteDao {

    public ClienteDaoImpl() {
        super(Cliente.class);
    }

    @Override
    public Cliente buscarCliente(String ruc) {

        Cliente cliente = null;

        Query q;

        q = em.createNamedQuery("Cliente.findByRucCliente");
        q.setParameter("rucCliente", ruc);

        try {
            cliente = (Cliente) q.getSingleResult();
        } catch (Exception ex) {

        }

        return cliente;

    }
}

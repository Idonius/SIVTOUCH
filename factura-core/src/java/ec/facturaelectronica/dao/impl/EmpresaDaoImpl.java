/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Plan;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class EmpresaDaoImpl extends GenericDaoImpl<Empresa, Integer> implements EmpresaDao {

    public EmpresaDaoImpl() {
        super(Empresa.class);
    }

    @Override
    public List<Empresa> getEmpresas() {
        List<Empresa> empresas;

        Query q = em.createNamedQuery("Empresa.findAll");
        q.setParameter("idEstadoCatalogo", EstadosGeneralesEnum.Activo.getOrden());
        empresas = q.getResultList();

        return empresas;
    }

    @Override
    public List<Empresa> getEmpresasMinusUsuario(Usuario usuario) {

        List<Empresa> empresas;

        Query q = em.createNamedQuery("Empresa.findAllMinusByUsuario");
        q.setParameter("idEstadoCatalogo", EstadosGeneralesEnum.Activo.getOrden());
        q.setParameter("idUsuario", usuario);

        empresas = q.getResultList();

        return empresas;

    }

    @Override
    public List<Empresa> getEmpresaPorPlan(final Plan plan) {
        List<Empresa> result = Collections.emptyList();
        Query qry = em.createNamedQuery("Empresa.findAllByPlan");
        if (qry != null) {
            qry.setParameter("plan", plan);
            result = qry.getResultList();
        }
        return result;
    }

    @Override
    public Empresa getEmpresaByRuc(String ruc) {
        Empresa empresa;

        Query q = em.createNamedQuery("Empresa.findAll");
        q.setParameter("idEstadoCatalogo", EstadosGeneralesEnum.Activo.getOrden());
        q.setParameter("rucEmpresa", ruc);

        empresa = (Empresa) q.getSingleResult();

        return empresa;
    }

}

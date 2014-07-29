/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.dao.PlanDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Plan;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.PlanService;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class PlanServiceImpl implements PlanService {

    @EJB
    private PlanDao planDao;

    @EJB
    private EmpresaDao empresaDao;
    
    @EJB
    private CatalogoDao catalogoDao;

    @Override
    public List<Plan> getTodosLosPlanes() throws ServicesException {
        List<Plan> result = Collections.emptyList();
        try {
            result = planDao.obtenerTodosLosPlanes();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return result;
    }

    @Override
    public void nuevoPlan(final Plan plan) throws ServicesException {
        try {
            planDao.insert(plan);
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    @Override
    public Empresa getEmpresaPorId(int empresa) throws ServicesException {
        Empresa result = new Empresa();
        try {
            result = empresaDao.load(empresa);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return result;
    }

    @Override
    public void modificarPlan(Plan plan) throws ServicesException {
        try {
            planDao.update(plan);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public void EliminarPlan(final Plan plan) throws ServicesException {
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Desactivado.getOrden());
            List<Empresa> empresas = empresaDao.getEmpresaPorPlan(plan);
            if (!empresas.isEmpty()){
                throw  new ServicesException("El plan se encuentra asociado a empresas activas, la operación no pudo ser concluida");
            }
            plan.setIdEstado(catalogo);
            planDao.update(plan);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }

    }
    
    

}

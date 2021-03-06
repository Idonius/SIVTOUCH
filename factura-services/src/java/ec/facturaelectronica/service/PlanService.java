/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Plan;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface PlanService {
    List<Plan> getTodosLosPlanes() throws ServicesException;
    void nuevoPlan(final Plan plan) throws ServicesException;
    Empresa getEmpresaPorId(final int empresa) throws ServicesException;
    void modificarPlan(final Plan plan) throws ServicesException;
    void EliminarPlan(final Plan plan) throws ServicesException;
}

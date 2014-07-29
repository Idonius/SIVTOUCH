/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.PlanDao;
import ec.facturaelectronica.model.Plan;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Armando
 */
@Stateless
public class PlanDaoImpl extends GenericDaoImpl<Plan, Integer> implements PlanDao, Serializable{

    public PlanDaoImpl() {
        super(Plan.class);
    }

    @Override
    public List<Plan> obtenerTodosLosPlanes() {
        List<Plan> result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("plan.findAll");
        if(qry != null && qry.getResultList().size() > 0){
            result = qry.getResultList();
        }
        return result;
    }
    
}

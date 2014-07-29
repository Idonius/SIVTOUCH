/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.GrupoCatalogoDao;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.GrupoCatalogo;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Armando
 */
@Stateless
public class GrupoCatalogoDaoImpl extends GenericDaoImpl<GrupoCatalogo, Integer> implements GrupoCatalogoDao{

    public GrupoCatalogoDaoImpl() {
        super(GrupoCatalogo.class);
    }

    @Override
    public List<Catalogo> obtenerCatalogosPorIdGrupo(final int grupoCatalogoId) {
        List<Catalogo> result = Collections.emptyList();
        Query qry = em.createNamedQuery("Catalogo.findByIdGrpCatalogo");
        if(qry.setParameter("idGrpCatalogo", grupoCatalogoId) != null && qry.getResultList().size() > 0){
            result = qry.getResultList();
        }
        return result;
    }
    
    
}

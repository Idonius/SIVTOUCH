/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;


import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.GrupoCatalogo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class CatalogoDaoImpl  extends GenericDaoImpl<Catalogo, Long> implements CatalogoDao {

    public CatalogoDaoImpl() {
        super(Catalogo.class);
    }

    @Override
    public List<Catalogo> getCatalogoByGrupo(GrupoCatalogo grupo) {
            
        List<Catalogo> catologos;
        
        Query q = em.createNamedQuery("Catalogo.findAllByGrupo");
     
        q.setParameter("idGrpCatalogo", grupo);
     
        catologos = q.getResultList();

        return catologos;
    }    
    
}

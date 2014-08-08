/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;


import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.GrupoCatalogo;
import java.util.Collections;
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
     
        q.setParameter("grupo", grupo);
     
        catologos = q.getResultList();

        return catologos;
    }  

    @Override
    public List<Catalogo> getCatalogoByGrupoByNombre(final GrupoCatalogo grupo, final String nombreCatalogo) {
        List<Catalogo> result = Collections.emptyList();
        System.out.println(String.format("Grupo: %s, NombreCatalogo: %s", grupo.getIdGrpCatalogo(), nombreCatalogo));
        Query qry = em.createNamedQuery("Catalogo.findByGrpCatalogoByNombreCatalogo");
        if(qry != null){
            qry
                    .setParameter("grpCatalogo", grupo)
                    .setParameter("nombre", nombreCatalogo);
            result = qry.getResultList();
            System.out.println("Cantidad de registros: " + result.size());
        }
        return result;
    }

    @Override
    public List<Catalogo> getCatalogoByGrupoTrans() {
        List<Catalogo> result = Collections.emptyList();
        
        Query qry = em.createNamedQuery("Catalogo.findAllByTrans");
        if(qry != null){
            result = qry.getResultList();
        }
        return result;
    }
    
    
    
}

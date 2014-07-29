/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;


import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.GrupoCatalogo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface CatalogoDao extends GenericDao<Catalogo , Long>{
    public List<Catalogo> getCatalogoByGrupo(GrupoCatalogo grupo);
    
}

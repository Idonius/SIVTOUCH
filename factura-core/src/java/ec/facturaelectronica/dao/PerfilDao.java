/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.Perfil;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface PerfilDao extends GenericDao<Perfil , Long>{
     public List<Perfil> getPerfiles();
}

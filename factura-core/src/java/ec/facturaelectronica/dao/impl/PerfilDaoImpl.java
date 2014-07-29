/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;


import ec.facturaelectronica.dao.PerfilDao;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class PerfilDaoImpl extends GenericDaoImpl<Perfil, Long> implements PerfilDao {

    public PerfilDaoImpl() {
        super(Perfil.class);
    }

    @Override
    public List<Perfil> getPerfiles() {
        List<Perfil> perfiles;

        Query q = em.createNamedQuery("Perfil.find");
        q.setParameter("idCatalogo", EstadosGeneralesEnum.Activo.getOrden() );
        perfiles = q.getResultList();

        return perfiles;
    }
}

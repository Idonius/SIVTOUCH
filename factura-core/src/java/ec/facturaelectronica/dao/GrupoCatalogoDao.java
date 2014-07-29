/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.GrupoCatalogo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface GrupoCatalogoDao extends GenericDao<GrupoCatalogo, Integer>{
    List<Catalogo> obtenerCatalogosPorIdGrupo(final int grupoCatalogoId);
}

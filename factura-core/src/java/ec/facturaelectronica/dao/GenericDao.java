/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;


/**
 *
 * @author Christian
 */

import java.io.Serializable;

public interface GenericDao<T, PK extends Serializable>  extends Serializable{
    
    /**
     * Inserta un objeto en la base de datos.
     *
     * @param object objeto a persistir
     */
    void insert(T o) ;

    /**
     * Actualizar un objeto en la base de datos.
     *
     * @param object
     */
    void update(T o) ;

    /**
     * Obtener un objeto almacenado utilizando su llave primaria.
     *
     * @param id llave primaria.
     * @return
     */
    T load(PK id);

    /**
     * Eliminar un objeto de la base de datos.
     *
     * @param object
     */
    void delete(T o);
    
    void flush();
    
    void refresh(T o);
    
    void detach (T o);

}

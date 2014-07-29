/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;


import ec.facturaelectronica.dao.GenericDao;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Christian
 */
public abstract class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    @PersistenceContext(unitName = "factura-corePU")
    protected EntityManager em;
    private Class<T> type;

    /**
     * Constructor por defecto.
     *
     * @param type tipo de objeto del DAO.
     */
    public GenericDaoImpl(Class<T> type) {

        this.type = type;
    }

    @Override
    public void delete(T o) {

        em.remove(o);

    }

    @Override
    public void insert(T o)  {

      
            em.persist(o);
       
    }

    @Override
    public T load(PK id) {
        return em.find(type, id);
    }

    @Override
    public void update(T o) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolation = validator.validate(o);
        if(constraintViolation.size() > 0){
            Iterator<ConstraintViolation<T>> iterator = constraintViolation.iterator();
            while(iterator.hasNext()){
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName()+"."+cv.getPropertyPath()+" "+cv.getMessage());
            }
        }else{
            em.merge(o);
        }
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void refresh(T o) {
        em.refresh(o);
    }

    @Override
    public void detach(T o) {
        em.detach(o);
    }
}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;

@SuppressWarnings("unchecked")
public abstract class JPANamedEntityDAO<T, ID> extends JPAGenericDAO<T, ID> implements NamedEntityDAO<T, ID> {

    protected JPANamedEntityDAO() {}

    @Override
    public T findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return (T) persistenceMethodsProvider.functionalMethod(entityManager ->
                (T)entityManager.createQuery("SELECT e FROM " + getPersistenceClass().getName() +
                        " e WHERE e.name = '" + name + "'").getResultList().get(0));
    }

}

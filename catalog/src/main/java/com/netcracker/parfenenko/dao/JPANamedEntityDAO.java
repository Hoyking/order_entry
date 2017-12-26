package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class JPANamedEntityDAO<T, ID> extends JPAGenericDAO<T, ID> implements NamedEntityDAO<T, ID> {

    protected JPANamedEntityDAO() {
    }

    @Override
    public T findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return (T) persistenceMethodsProvider.functionalMethod(entityManager ->
                (T) entityManager
                        .createQuery("SELECT e FROM " + getPersistenceClass().getName() +
                                " e WHERE e.name = :name")
                        .setParameter("name", name)
                        .getSingleResult()
        );
    }

    @Override
    public List<T> findByPartOfName(String part) throws PersistenceMethodException {
        return (List<T>) persistenceMethodsProvider.functionalMethod(entityManager ->
                (List<T>) entityManager
                        .createQuery("SELECT e FROM " + getPersistenceClass().getName() +
                                " e WHERE e.name LIKE CONCAT('%', :part, '%')")
                        .setParameter("part", part)
                        .getResultList()
        );
    }

}

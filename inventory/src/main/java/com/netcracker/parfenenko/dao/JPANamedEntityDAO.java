package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;

@SuppressWarnings("unchecked")
public abstract class JPANamedEntityDAO<T, ID> extends JPAGenericDAO<T, ID> implements NamedEntityDAO<T, ID> {

    private String findByNameQuery = "SELECT e FROM %s e WHERE e.name = :name";

    protected JPANamedEntityDAO() {}

    @Override
    public T findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for entity " + getPersistenceClass().getName() + " with name " + name;
        return (T) persistenceMethodsProvider.functionalMethod(entityManager ->
                        (T) entityManager
                                .createQuery(String.format(findByNameQuery, getPersistenceClass().getName()))
                                .setParameter("name", name)
                                .getSingleResult()
                , operation
        );
    }

}

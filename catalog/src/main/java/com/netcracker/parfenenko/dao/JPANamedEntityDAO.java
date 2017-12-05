package com.netcracker.parfenenko.dao;

public abstract class JPANamedEntityDAO<T, ID> extends JPAGenericDAO<T, ID> implements NamedEntityDAO<T, ID> {

    protected JPANamedEntityDAO() {}

    @Override
    public T findByName(String name) {
        return (T) persistenceMethodsProvider.functionalMethod(entityManager ->
                (T) entityManager.createQuery("SELECT e FROM " + getPersistenceClass().getName() +
                        " e WHERE e.name = '" + name + "'").getResultList().get(0));
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.util.PersistenceMethodsProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {
    
    protected PersistenceMethodsProvider persistenceMethodsProvider;
    private Class persistenceClass;

    protected JPAGenericDAO() {}

    protected Class getPersistenceClass() {
        return persistenceClass;
    }

    protected void setPersistenceClass(Class persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    @Autowired
    private void setPersistenceMethodsProvider(PersistenceMethodsProvider persistenceMethodsProvider) {
        this.persistenceMethodsProvider = persistenceMethodsProvider;
    }

    @Override
    public T save(T entity) {
        return (T) persistenceMethodsProvider.functionalMethod(entityManager -> {
                    entityManager.persist(entity);
                    return entity;
        });
    }

    @Override
    public T findById(ID id) {
        return (T) persistenceMethodsProvider.functionalMethod(entityManager ->
                (T) entityManager.find(persistenceClass, id));
    }

    @Override
    public List<T> findAll() {
        return (List<T>) persistenceMethodsProvider.functionalMethod(entityManager ->
                (List<T>) entityManager.createQuery("SELECT e FROM " +
                        persistenceClass.getName() + " e").getResultList());
    }

    @Override
    public T update(T entity) {
        return persistenceMethodsProvider.functionalMethod(entityManager -> {
                    entityManager.merge(entity);
                    return entity;
        });
    }

    @Override
    public void delete(ID id) {
        persistenceMethodsProvider.consumerMethod(entityManager -> {
                    T entity = (T) entityManager.find(persistenceClass, id);
                    entityManager.remove(entity);
        });
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.util.PersistenceMethodsProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@SuppressWarnings("unchecked")
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
    public T save(T entity) throws PersistenceMethodException {
        String operation = "saving entity " + persistenceClass.getName();
        return (T) persistenceMethodsProvider.functionalMethod(entityManager -> {
                    entityManager.persist(entity);
                    return entity;
        }, operation);
    }

    @Override
    public T findById(ID id) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for entity " + persistenceClass.getName() + " with id " + id;
        return (T) persistenceMethodsProvider.functionalMethod(entityManager ->
                (T) entityManager.find(persistenceClass, id), operation);
    }

    @Override
    public List<T> findAll() throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for all entities " + persistenceClass.getName();
        return (List<T>) persistenceMethodsProvider.functionalMethod(entityManager ->
                (List<T>) entityManager.createQuery("SELECT e FROM " +
                        persistenceClass.getName() + " e").getResultList(),
                operation);
    }

    @Override
    public T update(T entity) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "updating entity " + persistenceClass.getName();
        return persistenceMethodsProvider.functionalMethod(entityManager -> entityManager.merge(entity),
                operation);
    }

    @Override
    public void delete(ID id) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "deleting entity " + persistenceClass.getName() + " with id " + id;
        persistenceMethodsProvider.consumerMethod(entityManager -> {
                    T entity = (T) entityManager.find(persistenceClass, id);
                    entityManager.remove(entity);
        }, operation);
    }

}

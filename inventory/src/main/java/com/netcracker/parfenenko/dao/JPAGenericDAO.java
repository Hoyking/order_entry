package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.util.Transactions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {
    
    protected Transactions transactions;
    private Class persistenceClass;

    protected JPAGenericDAO() {}

    protected Class getPersistenceClass() {
        return persistenceClass;
    }

    protected void setPersistenceClass(Class persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    @Autowired
    private void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    @Override
    public T save(T entity) {
        return (T) transactions.startGenericTransaction(entityManager -> {
                    entityManager.persist(entity);
                    return entity;
        });
    }

    @Override
    public T findById(ID id) {
        return (T) transactions.startGenericTransaction(entityManager ->
                (T)entityManager.find(persistenceClass, id));
    }

    @Override
    public List<T> findAll() {
        return (List<T>) transactions.startGenericTransaction(entityManager ->
                (List<T>)entityManager.createQuery("SELECT e FROM " +
                        persistenceClass.getName() + " e").getResultList());
    }

    @Override
    public T update(T entity) {
        return transactions.startGenericTransaction(entityManager -> {
                    entityManager.merge(entity);
                    return entity;
        });
    }

    @Override
    public void delete(ID id) {
        transactions.startTransaction(entityManager -> {
                    T entity = (T)entityManager.find(persistenceClass, id);
                    entityManager.remove(entity);
        });
    }

}

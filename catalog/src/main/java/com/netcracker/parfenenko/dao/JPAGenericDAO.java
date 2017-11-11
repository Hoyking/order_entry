package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.provider.EntityManagerProvider;
import com.netcracker.parfenenko.util.Transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public abstract class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {

    private Class persistenceClass;

    protected JPAGenericDAO(Class persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    @Override
    public void save(T entity) {
        Transactions.startTransaction(EntityManagerProvider.getInstance().createEntityManager(),
                someEntityManager -> someEntityManager.persist(entity));
    }

    @Override
    public T findById(ID id) {
        return (T) Transactions.startGenericTransaction(EntityManagerProvider.getInstance().createEntityManager(),
                someEntityManager -> (T)someEntityManager.find(persistenceClass, id));
    }

    @Override
    public List<T> findAll() {
        return (List<T>) Transactions.startGenericTransaction(EntityManagerProvider.getInstance().createEntityManager(),
                someEntityManager -> (List<T>)someEntityManager.createQuery("SELECT e FROM " +
                        persistenceClass.getName() + " e").getResultList());
    }

    @Override
    public void update(T entity) {
        Transactions.startTransaction(EntityManagerProvider.getInstance().createEntityManager(),
                someEntityManager -> someEntityManager.merge(entity));
    }

    @Override
    public void delete(ID id) {
        Transactions.startTransaction(EntityManagerProvider.getInstance().createEntityManager(),
                someEntityManager -> {
                    T entity = (T)someEntityManager.find(persistenceClass, id);
                    someEntityManager.remove(entity);});
    }

}

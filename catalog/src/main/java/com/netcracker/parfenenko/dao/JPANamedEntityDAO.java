package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.provider.EntityManagerProvider;
import com.netcracker.parfenenko.util.Transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class JPANamedEntityDAO<T, ID> extends JPAGenericDAO<T, ID> implements NamedEntityDAO<T, ID> {

    private Class persistenceClass;

    protected JPANamedEntityDAO(Class persistenceClass) {
        super(persistenceClass);
        this.persistenceClass = persistenceClass;
    }

    @Override
    public T findByName(String name) {
        return (T) Transactions.startGenericTransaction(EntityManagerProvider.getInstance().createEntityManager(),
                someEntityManager -> (T)someEntityManager.createQuery("SELECT e FROM " + persistenceClass.getName() +
                        " e WHERE e.name = '" + name + "'").getResultList().get(0));
    }

}

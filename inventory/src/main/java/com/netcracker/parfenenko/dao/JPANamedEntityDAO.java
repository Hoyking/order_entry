package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.provider.EntityManagerProvider;

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
        T entity = null;
        EntityManager entityManager = EntityManagerProvider.getInstance().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entity = (T)entityManager.createQuery("SELECT e FROM " + persistenceClass.getName() +
                    " e WHERE e.name = " + name).getResultList().get(0);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return entity;
    }

}

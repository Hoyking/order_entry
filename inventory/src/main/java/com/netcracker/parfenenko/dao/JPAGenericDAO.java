package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.provider.EntityManagerProvider;

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
        EntityManager entityManager = EntityManagerProvider.getInstance().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
//            entityManager.flush();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public T findById(ID id) {
        T entity = null;
        EntityManager entityManager = EntityManagerProvider.getInstance().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entity = (T)entityManager.find(persistenceClass, id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return entity;
    }

    @Override
    public List<T> findAll() {
        List<T> entities = null;
        EntityManager entityManager = EntityManagerProvider.getInstance().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entities = (List<T>)entityManager.createQuery("SELECT e FROM " + persistenceClass.getName() + " e")
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return entities;
    }

    @Override
    public void update(T entity) {
        EntityManager entityManager = EntityManagerProvider.getInstance().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(entity);
            entityManager.flush();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(ID id) {
        EntityManager entityManager = EntityManagerProvider.getInstance().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T entity = (T)entityManager.find(persistenceClass, id);
            entityManager.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

}

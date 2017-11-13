package com.netcracker.parfenenko.provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProvider {

    private EntityManagerFactory entityManagerFactory;

    private EntityManagerProvider() {
        entityManagerFactory = Persistence.createEntityManagerFactory("catalog-unit");
    }

    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private static class Holder {
        static final EntityManagerProvider INSTANCE = new EntityManagerProvider();
    }

    public static EntityManagerProvider getInstance() {
        return Holder.INSTANCE;
    }

}

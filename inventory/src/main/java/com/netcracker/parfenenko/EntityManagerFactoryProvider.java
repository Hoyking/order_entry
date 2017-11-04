package com.netcracker.parfenenko;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProvider {

    private EntityManagerFactory entityManagerFactory;

    private EntityManagerFactoryProvider() {
        entityManagerFactory = Persistence.createEntityManagerFactory("inventory-unit");
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    private static class Holder {
        public static final EntityManagerFactoryProvider INSTANCE = new EntityManagerFactoryProvider();
    }

    public static EntityManagerFactoryProvider getInstance() {
        return Holder.INSTANCE;
    }

}

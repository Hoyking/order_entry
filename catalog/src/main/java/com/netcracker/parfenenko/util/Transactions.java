package com.netcracker.parfenenko.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Transactions {

    private Transactions() {}

    public static void startTransaction(EntityManager entityManager, Consumer<EntityManager> consumer) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            consumer.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public static <T> T startGenericTransaction(EntityManager entityManager, Function<EntityManager, T> function) {
        T result = null;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            result = function.apply(entityManager);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return result;
    }

}

package com.netcracker.parfenenko.util;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class Transactions {

    @PersistenceContext
    private EntityManager entityManager;

    public Transactions() {}

    public void startTransaction(Consumer<EntityManager> consumer) {
        consumer.accept(entityManager);
        entityManager.close();
    }

    public <T> T startGenericTransaction(Function<EntityManager, T> function) {
        T result = null;
        result = function.apply(entityManager);
        entityManager.close();
        return result;
    }

}

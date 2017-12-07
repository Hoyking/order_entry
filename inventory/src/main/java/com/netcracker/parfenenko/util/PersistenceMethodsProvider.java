package com.netcracker.parfenenko.util;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class PersistenceMethodsProvider {

    @PersistenceContext
    private EntityManager entityManager;

    public PersistenceMethodsProvider() {}

    public void consumerMethod(Consumer<EntityManager> consumer) {
        consumer.accept(entityManager);
        entityManager.close();
    }

    public <T> T functionalMethod(Function<EntityManager, T> function) {
        T result = null;
        result = function.apply(entityManager);
        entityManager.close();
        return result;
    }

}

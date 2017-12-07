package com.netcracker.parfenenko.util;

import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class PersistenceMethodsProvider {

    @PersistenceContext
    private EntityManager entityManager;

    public PersistenceMethodsProvider() {}

    public void consumerMethod(Consumer<EntityManager> consumer) throws PersistenceMethodException, EntityNotFoundException {
        try {
            consumer.accept(entityManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new EntityNotFoundException("Entity doesn't exist");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceMethodException();
        } finally {
            try {
                entityManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T functionalMethod(Function<EntityManager, T> function) throws PersistenceMethodException, EntityNotFoundException {
        T result;
        try {
            result = function.apply(entityManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new EntityNotFoundException("Entity doesn't exist");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceMethodException();
        } finally {
            try {
                entityManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            throw new EntityNotFoundException("Entity doesn't exist");
        }
        return result;
    }

}

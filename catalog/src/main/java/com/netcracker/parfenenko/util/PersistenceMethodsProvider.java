package com.netcracker.parfenenko.util;

import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("Duplicates")
@Component
public class PersistenceMethodsProvider {

    @PersistenceContext
    private EntityManager entityManager;

    public PersistenceMethodsProvider() {
    }

    public void consumerMethod(Consumer<EntityManager> consumer)
            throws PersistenceMethodException, EntityNotFoundException {
        try {
            consumer.accept(entityManager);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Entity doesn't exist");
        } catch (Exception e) {
            throw new PersistenceMethodException(e);
        }
    }

    public <T> T functionalMethod(Function<EntityManager, T> function)
            throws PersistenceMethodException, EntityNotFoundException {
        T result;
        try {
            result = function.apply(entityManager);
        } catch (IllegalArgumentException | NoResultException e) {
            throw new EntityNotFoundException("Entity doesn't exist");
        } catch (Exception e) {
            throw new PersistenceMethodException(e);
        }
        if (result == null) {
            throw new EntityNotFoundException("Entity doesn't exist");
        }
        return result;
    }

}

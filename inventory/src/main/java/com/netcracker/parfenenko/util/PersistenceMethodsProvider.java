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

    private static final Logger logger = LogManager.getLogger(PersistenceMethodsProvider.class);

    @PersistenceContext
    private EntityManager entityManager;

    public PersistenceMethodsProvider() {}

    public void consumerMethod(Consumer<EntityManager> consumer, String operation)
            throws PersistenceMethodException, EntityNotFoundException {
        logger.info("START OPERATION: " + operation);
        try {
            consumer.accept(entityManager);
        } catch (IllegalArgumentException e) {
            logger.error("There is an error occurred while executing operation of '" +
                    operation + "'. Stack trace:", e);
            throw new EntityNotFoundException("Entity doesn't exist");
        } catch (Exception e) {
            logger.error("There is an error occurred while executing operation of '" +
                    operation + "'. Stack trace:", e);
            throw new PersistenceMethodException();
        }
        logger.info("END OF OPERATION: " + operation);
    }

    public <T> T functionalMethod(Function<EntityManager, T> function, String operation)
            throws PersistenceMethodException, EntityNotFoundException {
        logger.info("START OPERATION: " + operation);
        T result;
        try {
            result = function.apply(entityManager);
        } catch (IllegalArgumentException | NoResultException e) {
            logger.error("There is an error occurred while executing operation of '" +
                    operation + "'. Stack trace:", e);
            throw new EntityNotFoundException("Entity doesn't exist");
        } catch (Exception e) {
            logger.error("There is an error occurred while executing operation of '" +
                    operation + "'. Stack trace:", e);
            throw new PersistenceMethodException();
        }
        if (result == null) {
            logger.error("There is an error occurred while executing operation of '" +
                    operation + "'. Entity doesn't exist");
            throw new EntityNotFoundException("Entity doesn't exist");
        }
        logger.info("END OF OPERATION: " + operation);
        return result;
    }

}

package com.netcracker.parfenenko.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class Transactions {

    private static Logger logger = LogManager.getLogger(Transactions.class);

    @PersistenceContext
    private EntityManager entityManager;

    public Transactions() {}

    public void startTransaction(Consumer<EntityManager> consumer) {
        logger.info("Start of operation");
        try {
            consumer.accept(entityManager);
            entityManager.close();
        } catch (Exception e) {
            logger.error("Failed to perform operation. The stacktrace of an exception: /n" +
                    Arrays.toString(e.getStackTrace()));
        }
        logger.info("End of operation");
    }

    public <T> T startGenericTransaction(Function<EntityManager, T> function) {
        logger.info("Start of operation");
        T result = null;
        try {
            result = function.apply(entityManager);
            entityManager.close();
        } catch (Exception e) {
            logger.error("Failed to perform operation. The stacktrace of an exception: /n" +
                    Arrays.toString(e.getStackTrace()));
        }
        logger.info("End of operation");
        return result;
    }

}

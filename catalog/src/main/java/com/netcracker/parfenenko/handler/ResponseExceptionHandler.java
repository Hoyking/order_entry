package com.netcracker.parfenenko.handler;

import com.netcracker.parfenenko.exception.EntityCreationException;
import com.netcracker.parfenenko.exception.EntityDeletingException;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(value = {PersistenceMethodException.class, EntityNotFoundException.class, IllegalArgumentException.class,
            TransactionSystemException.class, NoContentException.class, EntityDeletingException.class,
            EntityCreationException.class})
    public final ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        ResponseEntity<Object> responseEntity = null;
        if (e.getClass().equals(PersistenceMethodException.class)) {
            responseEntity = handleExceptionInternal(e, "Oops, something went wrong", new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
            logErrorMessage(e);
        } else if (e.getClass().equals(EntityNotFoundException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(),
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
            logErrorMessage(e);
        } else if (e.getClass().equals(IllegalArgumentException.class) || e.getClass().equals(EntityDeletingException.class)
                || e.getClass().equals(EntityCreationException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.BAD_REQUEST, request);
            logErrorMessage(e);
        } else if (e.getClass().equals(TransactionSystemException.class)) {
            Throwable cause = e.getCause();
            while (cause != null && !(cause.getClass().equals(ConstraintViolationException.class))) {
                cause = cause.getCause();
            }
            //noinspection ConstantConditions
            responseEntity = handleExceptionInternal((Exception) cause, validationErrorMessage((ConstraintViolationException) cause),
                    new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
            logErrorMessage((Exception) cause);
        } else if (e.getClass().equals(NoContentException.class)) {
            responseEntity = handleExceptionInternal(e, "", new HttpHeaders(), HttpStatus.NO_CONTENT, request);
        }
        return responseEntity;
    }

    private void logErrorMessage(Exception e) {
        LOGGER.error("There is an error occurred while executing operation. Stack trace: ", e);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private String validationErrorMessage(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        e.getConstraintViolations().forEach(constraintViolation -> {
            Class clazz = constraintViolation.getRootBeanClass();
            String name = constraintViolation.getRootBeanClass().getName();
            String[] splittedEntityName = constraintViolation.getRootBeanClass().getName().split("\\.");
            message
                    .append(splittedEntityName[splittedEntityName.length - 1])
                    .append(" ")
                    .append(constraintViolation.getPropertyPath())
                    .append(": ")
                    .append(constraintViolation.getMessage())
                    .append("\n");
        });
        return message.toString();
    }

}

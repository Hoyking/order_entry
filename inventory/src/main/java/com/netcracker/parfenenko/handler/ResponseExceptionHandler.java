package com.netcracker.parfenenko.handler;

import com.netcracker.parfenenko.exception.DocumentNotFoundException;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(value = {UpdateStatusException.class, DocumentNotFoundException.class,
            StatusSignException.class, TransactionSystemException.class, NoContentException.class})
    public final ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        ResponseEntity<Object> responseEntity = null;
        if (e.getClass().equals(DataAccessException.class)) {
            responseEntity = handleExceptionInternal(e, "Oops, something went wrong", new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        } else if (e.getClass().equals(DocumentNotFoundException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        } else if (e.getClass().equals(StatusSignException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.BAD_REQUEST, request);
        } else if (e.getClass().equals(UpdateStatusException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        } else if (e.getClass().equals(TransactionSystemException.class)) {
            Throwable cause = e.getCause();
            while (cause != null && !(cause.getClass().equals(ConstraintViolationException.class))) {
                cause = cause.getCause();
            }
            //noinspection ConstantConditions
            responseEntity = handleExceptionInternal((Exception) cause, validationErrorMessage((ConstraintViolationException) cause),
                    new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
            logErrorMessage((Exception) cause);
            return responseEntity;
        } else if (e.getClass().equals(NoContentException.class)) {
            return handleExceptionInternal(e, "", new HttpHeaders(), HttpStatus.NO_CONTENT, request);
        }
        logErrorMessage(e);
        return responseEntity;
    }

    private void logErrorMessage(Exception e) {
        LOGGER.error("There is an error occurred while executing operation. Stack trace: ", e);
    }

    @SuppressWarnings({"SpellCheckingInspection", "Duplicates"})
    private String validationErrorMessage(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        e.getConstraintViolations().forEach(constraintViolation -> {
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

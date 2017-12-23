package com.netcracker.parfenenko.handler;

import com.netcracker.parfenenko.exception.UpdateStatusException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(value = {PersistenceMethodException.class, EntityNotFoundException.class, UpdateStatusException.class,
            StatusSignException.class})
    public final ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        ResponseEntity<Object> responseEntity = null;
        if (e.getClass().equals(PersistenceMethodException.class)) {
            responseEntity = handleExceptionInternal(e, "Oops, something went wrong", new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        } else if (e.getClass().equals(EntityNotFoundException.class)) {
            responseEntity = handleExceptionInternal(e, "There is now information you are looking for",
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        } else if (e.getClass().equals(StatusSignException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.BAD_REQUEST, request);
        } else if (e.getClass().equals(UpdateStatusException.class)) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
        logErrorMessage(e);
        return responseEntity;
    }

    private void logErrorMessage(Exception e) {
        LOGGER.error("There is an error occurred while executing operation. Stack trace: ", e);
    }

}

package com.netcracker.parfenenko.handler;

import com.netcracker.parfenenko.exception.UpdateOrderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ResponseExceptionHandler.class);


    @ExceptionHandler(value = {UpdateOrderException.class, EntityNotFoundException.class, HttpClientErrorException.class,
            HttpServerErrorException.class})
    public final ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        String body;
        ResponseEntity<Object> response = null;
        if (e.getClass().equals(UpdateOrderException.class)) {
            body = e.getMessage();
            response = handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
        } else if (e.getClass().equals(EntityNotFoundException.class)) {
            body = e.getMessage();
            response = handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        } else if (e.getClass().equals(HttpClientErrorException.class) || e.getClass().equals(HttpServerErrorException.class)) {
            body = ((HttpClientErrorException) e).getResponseBodyAsString();
            int status = ((HttpClientErrorException) e).getRawStatusCode();
            response = handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.valueOf(status), request);
        }
        logErrorMessage(e);
        return response;
    }

    private void logErrorMessage(Exception e) {
        LOGGER.error("There is an error occurred while executing operation. Stack trace: ", e);
    }

}

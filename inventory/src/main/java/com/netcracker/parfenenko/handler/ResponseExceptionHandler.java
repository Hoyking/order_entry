package com.netcracker.parfenenko.handler;

import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
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

    @ExceptionHandler(value = {PersistenceMethodException.class, EntityNotFoundException.class, PayForOrderException.class,
            PaymentStatusException.class})
    public final ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        ResponseEntity<Object> responseEntity = null;
        if (e instanceof PersistenceMethodException) {
            responseEntity = handleExceptionInternal(e, "Oops, something went wrong", new HttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        } else if (e instanceof EntityNotFoundException) {
            responseEntity = handleExceptionInternal(e, "There is now information you are looking for",
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        } else if (e instanceof PaymentStatusException) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.BAD_REQUEST, request);
        } else if (e instanceof PayForOrderException) {
            responseEntity = handleExceptionInternal(e, e.getMessage(), new HttpHeaders(),
                    HttpStatus.CONFLICT, request);
        }
        return responseEntity;
    }

}

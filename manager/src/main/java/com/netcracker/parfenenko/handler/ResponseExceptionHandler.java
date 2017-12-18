package com.netcracker.parfenenko.handler;

import com.netcracker.parfenenko.exception.UpdateOrderException;
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


    @ExceptionHandler(value = {UpdateOrderException.class, EntityNotFoundException.class})
    public final ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        String body;
        ResponseEntity<Object> response = null;
        if (e instanceof UpdateOrderException) {
            body = e.getMessage();
            response = handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
        } else if (e instanceof EntityNotFoundException) {
            body = e.getMessage();
            response = handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        }
        return response;
    }

}
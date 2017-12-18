package com.netcracker.parfenenko.exception;

public class UpdateOrderException extends RuntimeException {

    public UpdateOrderException(String reason) {
        super(reason);
    }

}

package com.netcracker.parfenenko.exception;

public class PayForOrderException extends RuntimeException {

    public PayForOrderException(String reason) {
        super(reason);
    }

}

package com.netcracker.parfenenko.exception;

public class StatusSignException extends RuntimeException {

    public StatusSignException() {
        super("Invalid payment sign");
    }

    public StatusSignException(String message) {
        super(message);
    }

}

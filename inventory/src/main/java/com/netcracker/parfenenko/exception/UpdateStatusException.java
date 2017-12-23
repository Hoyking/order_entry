package com.netcracker.parfenenko.exception;

public class UpdateStatusException extends RuntimeException {

    public UpdateStatusException(String reason, Throwable cause) {
        super(reason, cause);
    }

}

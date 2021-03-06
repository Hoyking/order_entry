package com.netcracker.parfenenko.exception;

public class PersistenceMethodException extends RuntimeException {

    public PersistenceMethodException(Throwable cause) {
        super("Something went wrong while executing persistence operation", cause);
    }

}

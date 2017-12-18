package com.netcracker.parfenenko.exception;

public class PersistenceMethodException extends RuntimeException {

    public PersistenceMethodException() {
        super("Something went wrong while executing persistence operation");
    }

    public PersistenceMethodException(String reason) {
        super(reason);
    }

}

package com.netcracker.parfenenko.exception;

public class PersistenceMethodException extends Exception {

    public PersistenceMethodException() {
        super("Something went wrong while executing persistence operation");
    }

    public PersistenceMethodException(String reason) {
        super(reason);
    }

}

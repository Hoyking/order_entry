package com.netcracker.parfenenko.exception;

public class EntityDeletingException extends RuntimeException {

    public EntityDeletingException(String reason) {
        super(reason);
    }

}

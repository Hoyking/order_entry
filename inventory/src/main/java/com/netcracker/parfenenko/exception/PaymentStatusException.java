package com.netcracker.parfenenko.exception;

public class PaymentStatusException extends RuntimeException {

    public PaymentStatusException() {
        super("Invalid payment status");
    }

}

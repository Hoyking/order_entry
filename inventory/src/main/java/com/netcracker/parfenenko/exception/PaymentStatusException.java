package com.netcracker.parfenenko.exception;

public class PaymentStatusException extends Exception {

    public PaymentStatusException() {
        super("Invalid payment status");
    }

}

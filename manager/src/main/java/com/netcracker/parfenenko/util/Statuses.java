package com.netcracker.parfenenko.util;

public enum Statuses {

    OPENED(0),
    PAID(1),
    CANCELED(2),
    REJECTED_PAYMENT(3);

    private final int VALUE;

    Statuses(int value) {
        this.VALUE = value;
    }

    public int value() {
        return VALUE;
    }

    public static boolean consists(int value) {
        return value == OPENED.VALUE || value == PAID.VALUE || value == CANCELED.VALUE || value == REJECTED_PAYMENT.VALUE;
    }

}

package com.netcracker.parfenenko.util;

public enum Statuses {

    OPENED("OPENED"),
    PAID("PAID"),
    CANCELED("CANCELED"),
    REJECTED_PAYMENT("REJECTED_PAYMENT");

    private final String VALUE;

    Statuses(String value) {
        this.VALUE = value;
    }

    public String value() {
        return VALUE;
    }

    public static boolean consists(String value) {
        return value.equals(OPENED.VALUE) || value.equals(PAID.VALUE) || value.equals(CANCELED.VALUE) || value.equals(REJECTED_PAYMENT.VALUE);
    }

}

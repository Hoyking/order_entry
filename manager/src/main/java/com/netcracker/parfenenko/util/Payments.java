package com.netcracker.parfenenko.util;

public enum Payments {

    UNPAID(0),

    PAID(1),

    REJECTED(2);

    private final int VALUE;

    Payments(int value) {
        this.VALUE = value;
    }

    public int value() {
        return VALUE;
    }

    public static boolean consists(int value) {
        return value == UNPAID.VALUE || value == PAID.VALUE || value == REJECTED.VALUE;
    }

}

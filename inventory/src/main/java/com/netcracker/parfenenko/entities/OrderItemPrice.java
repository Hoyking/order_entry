package com.netcracker.parfenenko.entities;

public class OrderItemPrice {

    private double value;

    public OrderItemPrice() {}

    public OrderItemPrice(int value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}

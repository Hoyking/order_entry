package com.netcracker.parfenenko.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderItemPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double value;

    public OrderItemPrice() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}

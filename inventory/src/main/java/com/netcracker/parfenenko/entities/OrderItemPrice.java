package com.netcracker.parfenenko.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemPrice that = (OrderItemPrice) o;
        return id == that.id &&
                Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

}

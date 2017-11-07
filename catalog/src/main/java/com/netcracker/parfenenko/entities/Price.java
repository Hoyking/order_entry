package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double value;

    public Price() {}

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
        Price price = (Price) o;
        return id == price.id &&
                Double.compare(price.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}

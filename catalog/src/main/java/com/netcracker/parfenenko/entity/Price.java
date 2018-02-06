package com.netcracker.parfenenko.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Price extends IdentifiedEntity {

    @NotNull
    @Min(0)
    private double value;

    public Price() {}

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

package com.netcracker.parfenenko.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class OrderItem extends NamedEntity {

    @NotNull
    @Min(0)
    private double price;
    @NotNull
    private String category;
    @Size(max = 500)
    @NotNull
    private String description;

    public OrderItem() {}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                Double.compare(orderItem.price, price) == 0 &&
                Objects.equals(category, orderItem.category) &&
                Objects.equals(name, orderItem.name) &&
                Objects.equals(description, orderItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, category, name, description);
    }

}

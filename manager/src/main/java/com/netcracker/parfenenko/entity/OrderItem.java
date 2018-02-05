package com.netcracker.parfenenko.entity;

import java.util.Objects;

public class OrderItem {

    private double price;
    private String category;
    private String description;
    private String name;
    private String id;

    public OrderItem() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        return id.equals(orderItem.id) &&
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

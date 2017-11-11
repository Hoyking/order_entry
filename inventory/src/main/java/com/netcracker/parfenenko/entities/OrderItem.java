package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private double price;
    private String category;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;
    private String name;
    private String description;

    public OrderItem() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

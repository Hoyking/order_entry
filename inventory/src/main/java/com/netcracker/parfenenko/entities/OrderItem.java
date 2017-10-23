package com.netcracker.parfenenko.entities;

import java.util.List;

public class OrderItem {

    private OrderItemPrice price;
    private OrderItemCategory category;
    private List<OrderItemTag> tags;
    private long id;
    private String name;
    private String description;

    public OrderItem() {}

    public OrderItemPrice getPrice() {
        return price;
    }

    public void setPrice(OrderItemPrice price) {
        this.price = price;
    }

    public OrderItemCategory getCategory() {
        return category;
    }

    public void setCategory(OrderItemCategory category) {
        this.category = category;
    }

    public List<OrderItemTag> getTags() {
        return tags;
    }

    public void setTags(List<OrderItemTag> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

}

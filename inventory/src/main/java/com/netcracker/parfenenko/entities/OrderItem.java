package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "priceId", nullable = false)
    private OrderItemPrice price;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId", nullable = false)
    private OrderItemCategory category;
    @OneToMany
    @JoinTable(name = "tag_links",
               joinColumns = @JoinColumn(name = "orderItemId"),
               inverseJoinColumns = @JoinColumn(name = "tagId"))
    private List<OrderItemTag> tags;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                Objects.equals(price, orderItem.price) &&
                Objects.equals(category, orderItem.category) &&
                Objects.equals(name, orderItem.name) &&
                Objects.equals(description, orderItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, category, name, description);
    }

}

package com.netcracker.parfenenko.entity;

import java.util.Objects;

public class Offer extends NamedEntity {

    private Price price;
    private Category category;
    private String description;
    private boolean available = true;

    public Offer() {}

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id &&
                Objects.equals(price, offer.price) &&
                Objects.equals(category, offer.category) &&
                Objects.equals(name, offer.name) &&
                Objects.equals(description, offer.description) &&
                Objects.equals(available, offer.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, category, name, description, available);
    }

}

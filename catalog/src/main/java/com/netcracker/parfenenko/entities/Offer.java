package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Offer extends NamedEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Price price;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Category category;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Tag> tags;
    private String description;

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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
        Offer offer = (Offer) o;
        return id == offer.id &&
                Objects.equals(price, offer.price) &&
                Objects.equals(category, offer.category) &&
                Objects.equals(name, offer.name) &&
                Objects.equals(description, offer.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, category, name, description);
    }

}

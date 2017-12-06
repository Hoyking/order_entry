package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@NamedQueries(
        @NamedQuery(name = "findTags",
                query = "SELECT e.tags FROM Offer e WHERE e.id = ?1"
        )
)
public class Offer extends NamedEntity {

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Price price;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Category category;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<Tag> tags;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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

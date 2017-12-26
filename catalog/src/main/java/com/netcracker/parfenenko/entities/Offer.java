package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "findTags",
                query = "SELECT e.tags FROM Offer e WHERE e.id = ?1"
        ),
        @NamedQuery(name = "findByAllFilters",
                query = "SELECT DISTINCT o FROM Offer o JOIN Tag t " +
                        "ON (t.name IN :tags AND t MEMBER OF o.tags) " +
                        "WHERE o.category.id IN :categories " +
                        "AND o.price.value >= :fromPrice AND o.price.value <= :toPrice"
        ),
        @NamedQuery(name = "findByCategoriesAndPrice",
                query = "SELECT DISTINCT o FROM Offer o WHERE o.category.id IN :categories " +
                        "AND o.price.value >= :fromPrice AND o.price.value <= :toPrice"
        ),
        @NamedQuery(name = "findByTagsAndPrice",
                query = "SELECT DISTINCT o FROM Offer o JOIN Tag t " +
                        "ON (t.name IN :tags AND t MEMBER OF o.tags) " +
                        "WHERE o.price.value >= :fromPrice AND o.price.value <= :toPrice "
        ),
        @NamedQuery(name = "findByPrice",
                query = "SELECT DISTINCT o FROM Offer o WHERE o.price.value >= :fromPrice " +
                        "AND o.price.value <= :toPrice"
        ),
        @NamedQuery(name = "findByTags",
                query = "SELECT DISTINCT o FROM Offer o JOIN Tag t ON (t.name IN :tags AND t MEMBER OF o.tags)"
        ),
        @NamedQuery(name = "findByCategories",
                query = "SELECT DISTINCT o FROM Offer o WHERE o.category.id IN :categories "
        ),
        @NamedQuery(name = "findByCategoriesAndTags",
                query = "SELECT DISTINCT o FROM Offer o JOIN Tag t " +
                        "ON (t.name IN :tags AND t MEMBER OF o.tags) " +
                        "WHERE o.category.id IN :categories "
        )
})
public class Offer extends NamedEntity {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Price price;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private Category category;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<Tag> tags;
    @NotNull
    @Size(max = 500)
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

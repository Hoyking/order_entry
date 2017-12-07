package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "removeOfferFromCategory",
                query = "UPDATE Offer offer SET offer.category = NULL WHERE offer.category.id = ?1 AND offer.id = ?2"
        )
)
public class Category extends NamedEntity {

    public Category() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}

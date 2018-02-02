package com.netcracker.parfenenko.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "findOffersByTag",
                query = "SELECT e FROM Offer e JOIN Tag c ON c.name = ?1 AND c MEMBER OF e.tags"
        )
)
public class Tag extends NamedEntity {

    public Tag() {}

    @Size(min = 2, max = 25)
    @Column(unique = false)
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}

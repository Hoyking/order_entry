package com.netcracker.parfenenko.entities;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Tag extends NamedEntity {

    public Tag() {}

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

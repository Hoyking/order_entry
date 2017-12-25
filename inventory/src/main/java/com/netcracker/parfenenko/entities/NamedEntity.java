package com.netcracker.parfenenko.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class NamedEntity extends IdentifiedEntity {

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 30)
    protected String name;

    public NamedEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

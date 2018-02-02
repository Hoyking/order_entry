package com.netcracker.parfenenko.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class NamedEntity extends IdentifiedEntity {

    protected String name;

    protected NamedEntity() {}

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

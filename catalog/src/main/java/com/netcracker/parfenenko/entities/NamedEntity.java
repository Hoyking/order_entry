package com.netcracker.parfenenko.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedEntity extends IdentifiedEntity {

    @Column(unique = true)
    protected String name;

    public NamedEntity() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

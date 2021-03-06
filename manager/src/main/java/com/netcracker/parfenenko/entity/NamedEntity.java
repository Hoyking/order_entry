package com.netcracker.parfenenko.entity;

public abstract class NamedEntity extends IdentifiedEntity {

    protected String name;

    public NamedEntity() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

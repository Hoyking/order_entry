package com.netcracker.parfenenko.entity;

public abstract class IdentifiedEntity {

    protected long id;

    public IdentifiedEntity() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}

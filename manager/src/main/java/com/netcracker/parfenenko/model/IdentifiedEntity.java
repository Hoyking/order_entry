package com.netcracker.parfenenko.model;

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

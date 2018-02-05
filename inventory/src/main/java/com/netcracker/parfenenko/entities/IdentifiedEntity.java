package com.netcracker.parfenenko.entities;

import org.springframework.data.annotation.Id;

public abstract class IdentifiedEntity {

    @Id
    protected String id;

    protected IdentifiedEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

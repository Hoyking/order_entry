package com.netcracker.parfenenko.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdentifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected long id;

    public IdentifiedEntity() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}

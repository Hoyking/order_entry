package com.netcracker.parfenenko.dto;

import lombok.Data;

@Data
public abstract class NamedDto extends IdentifiedDto {

    protected String name;

    public NamedDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

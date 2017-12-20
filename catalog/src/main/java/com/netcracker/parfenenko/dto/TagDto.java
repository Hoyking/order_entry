package com.netcracker.parfenenko.dto;

import lombok.Data;

@Data
public class TagDto {

    private String name;

    public TagDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

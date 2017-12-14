package com.netcracker.parfenenko.dto;

import lombok.Data;

@Data
public abstract class IdentifiedDto {

    protected long id;

    public IdentifiedDto() {}

}

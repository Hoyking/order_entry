package com.netcracker.parfenenko.dto;

import lombok.Data;

@Data
public abstract class NamedDto extends IdentifiedDto {

    protected String name;

}

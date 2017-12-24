package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UpdateOrderDto {

    private long id;
    private String description;
    private String customerMail;

}

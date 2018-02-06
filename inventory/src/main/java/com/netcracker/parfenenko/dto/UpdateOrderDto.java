package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UpdateOrderDto {

    private String id;
    private String description;
    private String customerMail;

}

package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class FreshOrderDto {

    private String description;
    private String customerMail;

}

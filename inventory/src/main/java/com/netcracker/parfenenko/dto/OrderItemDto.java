package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OrderItemDto {

    private String name;
    private String description;
    private String category;
    private double price;

}

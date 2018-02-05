package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OrderDto {

    private String id;
    private String name;
    private String description;
    private double totalPrice;
    private String customerMail;
    private String orderDate;
    private String paymentStatus;

}

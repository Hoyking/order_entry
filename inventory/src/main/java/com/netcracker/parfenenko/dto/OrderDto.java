package com.netcracker.parfenenko.dto;

import lombok.Data;

@Data
public class OrderDto {

    private long id;
    private String name;
    private String description;
    private double totalPrice;
    private String customerMail;
    private String orderDate;
    private int paymentStatus;

}
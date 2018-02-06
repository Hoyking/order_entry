package com.netcracker.parfenenko.entity;

import com.netcracker.parfenenko.util.Statuses;

import java.util.Objects;

public class Order {

    private String description;
    private double totalPrice;
    private String customerMail;
    private String orderDate;
    private String paymentStatus = Statuses.OPENED.value();
    private String name;
    private String id;

    public Order() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id) &&
                Double.compare(order.totalPrice, totalPrice) == 0 &&
                Objects.equals(customerMail, order.customerMail) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(name, order.name) &&
                Objects.equals(description, order.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalPrice, customerMail, orderDate, name, description);
    }

}

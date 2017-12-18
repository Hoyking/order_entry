package com.netcracker.parfenenko.entity;

import com.netcracker.parfenenko.util.Payments;

import java.util.Objects;

public class Order extends NamedEntity {

    private String description;
    private double totalPrice;
    private String customerMail;
    private String orderDate;
    private int paymentStatus = Payments.UNPAID.value();

    public Order() {}

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

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
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

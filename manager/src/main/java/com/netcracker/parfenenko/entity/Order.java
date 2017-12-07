package com.netcracker.parfenenko.entity;

import com.netcracker.parfenenko.util.Payments;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Data
public class Order extends NamedEntity {

    private List<OrderItem> orderItems;
    private String description;
    private double totalPrice;
    private String customerMail;
    private String orderDate;
    private String paymentSign;
    private int paymentStatus = Payments.UNPAID.value();

    public Order() {}

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
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

    public String getPaymentSign() {
        return paymentSign;
    }

    public void setPaymentSign(String paymentSign) {
        this.paymentSign = paymentSign;
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
                Objects.equals(orderItems, order.orderItems) &&
                Objects.equals(customerMail, order.customerMail) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(paymentSign, order.paymentSign) &&
                Objects.equals(name, order.name) &&
                Objects.equals(description, order.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderItems, totalPrice, customerMail, orderDate, paymentSign, name, description);
    }

}

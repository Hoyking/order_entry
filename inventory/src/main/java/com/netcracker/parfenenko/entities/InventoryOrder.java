package com.netcracker.parfenenko.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class InventoryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    private String name;
    private String description;
    private double totalPrice;
    private String customerMail;
    private String orderDate;
    private String paymentSign;

    public InventoryOrder() {}

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryOrder order = (InventoryOrder) o;
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

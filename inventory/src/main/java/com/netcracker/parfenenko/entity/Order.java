package com.netcracker.parfenenko.entity;

import com.netcracker.parfenenko.util.Statuses;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "inventory_order", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "findOrderItems",
                query = "SELECT e.orderItems FROM com.netcracker.parfenenko.entity.Order e WHERE e.id = ?1"
        ),
        @NamedQuery(name = "findOrderItem",
                query = "SELECT e FROM OrderItem e WHERE e.id = ?1"
        )
})
public class Order extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;
    @Size(max = 100)
    @NotNull
    private String description;
    private double totalPrice;
    @Email
    @NotNull
    private String customerMail;
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private String orderDate;
    private String paymentStatus = Statuses.OPENED.value();

    public Order() {}

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
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
        return id == order.id &&
                Double.compare(order.totalPrice, totalPrice) == 0 &&
                Objects.equals(orderItems, order.orderItems) &&
                Objects.equals(customerMail, order.customerMail) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(name, order.name) &&
                Objects.equals(description, order.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderItems, totalPrice, customerMail, orderDate, name, description);
    }

}

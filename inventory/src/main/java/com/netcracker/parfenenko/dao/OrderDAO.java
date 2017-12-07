package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;

import java.util.List;
import java.util.Set;

public interface OrderDAO extends NamedEntityDAO<Order, Long> {

    Set<OrderItem> findOrderItems(long orderId);

    Order addOrderItem(long orderId, OrderItem orderItem);

    Order removeOrderItem(long orderId, long orderItemId);

    List<Order> findOrdersByPaymentStatus(int paymentStatus) throws PaymentStatusException;

    Order payForOrder(long id) throws PayForOrderException;

}

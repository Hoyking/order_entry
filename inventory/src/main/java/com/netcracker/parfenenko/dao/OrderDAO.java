package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;

import java.util.List;

public interface OrderDAO extends NamedEntityDAO<Order, Long> {

    Order addOrderItem(long orderId, OrderItem orderItem);

    Order removeOrderItem(long orderId, long orderItemId);

    List<Order> findOrdersByPaymentStatus(int paymentStatus) throws PaymentStatusException;

    Order payForOrder(long id) throws PayForOrderException;

}

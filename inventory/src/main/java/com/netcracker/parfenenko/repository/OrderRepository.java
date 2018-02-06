package com.netcracker.parfenenko.repository;

import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;

import java.util.List;
import java.util.Set;

public interface OrderRepository {

    Order save(Order entity);

    Order findById(String id);

    List<Order> findAll();

    Order update(Order entity);

    void delete(String id);

    Order findByName(String name);

    Set<OrderItem> findOrderItems(String orderId);

    Order removeOrderItem(String orderId, String orderItemId);

    List<Order> findOrdersByPaymentStatus(String paymentStatus);

}

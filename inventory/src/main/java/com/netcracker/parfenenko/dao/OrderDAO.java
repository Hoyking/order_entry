package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;

public interface OrderDAO extends NamedEntityDAO<Order, Long> {

    Order addOrderItem(long orderId, OrderItem orderItem);

    Order removeOrderItem(long orderId, OrderItem orderItem);

}

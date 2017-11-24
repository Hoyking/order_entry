package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public class JPAOrderDAO extends JPANamedEntityDAO<Order, Long> implements OrderDAO {

    public JPAOrderDAO() {
        super.setPersistenceClass(Order.class);
    }

    @Override
    public Order addOrderItem(long orderId, OrderItem orderItem) {
        Order order = findById(orderId);
        order.getOrderItems().add(orderItem);
        return update(order);
    }

    @Override
    public Order removeOrderItem(long orderId, OrderItem orderItem) {
        Order order = findById(orderId);
        order.getOrderItems().remove(orderItem);
        return update(order);
    }

}

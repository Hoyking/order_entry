package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

public interface OrderDAO extends NamedEntityDAO<Order, Long> {

    Set<OrderItem> findOrderItems(long orderId) throws PersistenceMethodException, EntityNotFoundException;

    Order addOrderItem(long orderId, OrderItem orderItem) throws PersistenceMethodException, EntityNotFoundException;

    Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException;

    List<Order> findOrdersByPaymentStatus(int paymentStatus) throws PaymentStatusException, PersistenceMethodException,
            EntityNotFoundException;

    Order payForOrder(long id) throws PayForOrderException, PersistenceMethodException, EntityNotFoundException;

}

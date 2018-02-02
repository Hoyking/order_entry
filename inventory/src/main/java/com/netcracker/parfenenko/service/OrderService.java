package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public abstract class OrderService {

    protected static final Logger LOGGER = LogManager.getLogger(JPAOrderService.class);

    protected final String started = "Operation of {} started";
    protected final String finished = "Operation of {} finished";

    protected final String save = "saving order";
    protected final String findById = "searching for order with id %s";
    protected final String findByName = "searching for order with name %s";
    protected final String findAll = "searching for all orders";
    protected final String update = "updating an order";
    protected final String delete = "deleting an order with id %s";
    protected final String findOrderItems = "searching for order items of an order with id %s";
    protected final String addOrderITem = "adding order item to the order with id %s";
    protected final String removeOrderItem = "removing order item from the order with id %s";
    protected final String findOrdersByPaymentStatus = "searching for orders with payment status %s";
    protected final String updateStatus = "updating payment status of the order with id %s";
    protected final String countTotalPrice = "counting total price of the order with id %s";

    public abstract Order save(Order order) throws PersistenceMethodException;

    public abstract Order findById(long id) throws PersistenceMethodException, EntityNotFoundException;

    public abstract Order findByName(String name) throws PersistenceMethodException, EntityNotFoundException;

    public abstract List<Order> findAll() throws PersistenceMethodException, EntityNotFoundException;

    public abstract Order update(Order order) throws PersistenceMethodException, EntityNotFoundException;

    public abstract void delete(long id) throws PersistenceMethodException, EntityNotFoundException;

    public abstract Set<OrderItem> findOrderItems(long orderId) throws PersistenceMethodException, EntityNotFoundException;

    public abstract Order addOrderItem(long orderId, OrderItem orderItem) throws PersistenceMethodException, EntityNotFoundException;

    public abstract Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException;

    public abstract List<Order> findOrdersByPaymentStatus(String paymentStatus) throws StatusSignException, PersistenceMethodException,
            EntityNotFoundException;

    public abstract Order updateStatus(long id, String status) throws UpdateStatusException, PersistenceMethodException,
            EntityNotFoundException, StatusSignException;

    public abstract Order countTotalPrice(long orderId);

}

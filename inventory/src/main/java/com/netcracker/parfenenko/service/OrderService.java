package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import com.netcracker.parfenenko.util.Statuses;

import java.util.List;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public abstract class OrderService {

    protected final String STARTED = "Operation of {} started";
    protected final String FINISHED = "Operation of {} finished";

    protected final String SAVE = "saving order";
    protected final String FIND_BY_ID = "searching for order with id %s";
    protected final String FIND_BY_NAME = "searching for order with name %s";
    protected final String FIND_ALL = "searching for all orders";
    protected final String UPDATE = "updating an order";
    protected final String DELETE = "deleting an order with id %s";
    protected final String FIND_ORDER_ITEMS = "searching for order items of an order with id %s";
    protected final String ADD_ORDER_ITEM = "adding order item to the order with id %s";
    protected final String REMOVE_ORDER_ITEM = "removing order item from the order with id %s";
    protected final String FIND_ORDERS_BY_PAYMENT_STATUS = "searching for orders with payment status %s";
    protected final String UPDATE_STATUS = "updating payment status of the order with id %s";
    protected final String COUNT_TOTAL_PRICE = "counting total price of the order with id %s";

    public abstract Order save(Order order);

    public abstract Order findById(String id);

    public abstract Order findByName(String name);

    public abstract List<Order> findAll();

    public abstract Order update(Order order);

    public abstract void delete(String id);

    public abstract Set<OrderItem> findOrderItems(String orderId);

    public abstract Order addOrderItem(String orderId, OrderItem orderItem);

    public abstract Order removeOrderItem(String orderId, String orderItemId);

    public abstract List<Order> findOrdersByPaymentStatus(String paymentStatus) throws StatusSignException;

    public abstract Order updateStatus(String id, String status) throws UpdateStatusException, StatusSignException;

    public abstract Order countTotalPrice(String orderId);

    protected void isValidStatus(String status) throws StatusSignException {
        if (!Statuses.consists(status)) {
            throw new StatusSignException();
        }
    }

    protected void isValidStatus(String currentStatus, String newStatus) throws StatusSignException {
        isValidStatus(newStatus);

        final String OPENED = Statuses.OPENED.value();
        final String PAID = Statuses.PAID.value();
        final String CANCELED = Statuses.CANCELED.value();
        final String REJECTED = Statuses.REJECTED_PAYMENT.value();

        final String OPENED_MESSAGE = "Opened order can be changed either to paid or canceled";
        final String REJECTED_MESSAGE = "Rejected payment can be changed either to paid or canceled";
        final String CANCELED_MESSAGE = "Canceled order can't be changed";
        final String PAID_MESSAGE = "Paid order can't be changed";

        if (currentStatus.equals(OPENED)) {
            if (newStatus.equals(OPENED) || newStatus.equals(REJECTED)) {
                throw new StatusSignException(OPENED_MESSAGE);
            }
        }
        if (currentStatus.equals(REJECTED)) {
            if (newStatus.equals(OPENED) || newStatus.equals(REJECTED)) {
                throw new StatusSignException(REJECTED_MESSAGE);
            }
        }
        if (currentStatus.equals(PAID)) {
            throw new StatusSignException(PAID_MESSAGE);
        }
        if (currentStatus.equals(CANCELED)) {
            throw new StatusSignException(CANCELED_MESSAGE);
        }
    }

}

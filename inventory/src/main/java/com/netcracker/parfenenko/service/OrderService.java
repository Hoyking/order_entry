package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OrderDAO;
import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import com.netcracker.parfenenko.util.Statuses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("FieldCanBeLocal")
@Service
@Transactional
public class OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);

    private OrderDAO orderDAO;

    private String started = "Operation of {} started";
    private String finished = "Operation of {} finished";

    private String save = "saving order";
    private String findById = "searching for order with id %s";
    private String findByName = "searching for order with name %s";
    private String findAll = "searching for all orders";
    private String update = "updating an order";
    private String delete = "deleting an order with id %s";
    private String findOrderItems = "searching for order items of an order with id %s";
    private String addOrderITem = "adding order item to the order with id %s";
    private String removeOrderItem = "removing order item from the order with id %s";
    private String findOrdersByPaymentStatus = "searching for orders with payment status %s";
    private String updateStatus = "updating payment status of the order with id %s";

    @Autowired
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Order save(Order order) throws PersistenceMethodException {
        LOGGER.info(started, save);
        order.setOrderItems(new HashSet<>(0));
        order.setTotalPrice(0);
        order.setOrderDate(new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
        order = orderDAO.save(order);
        order.setName("Order #" + order.getId());
        order = orderDAO.update(order);
        LOGGER.info(finished, save);
        return order;
    }

    @Transactional(readOnly = true)
    public Order findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findById, id));
        Order order;
        try {
            order = orderDAO.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.info(finished, String.format(findById, id));
            throw new NoContentException();
        }
        LOGGER.info(finished, String.format(findById, id));
        return order;
    }

    @Transactional(readOnly = true)
    public Order findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findByName, name));
        Order order;
        try {
            order = orderDAO.findByName(name);
        } catch (EntityNotFoundException e) {
            LOGGER.info(finished, String.format(findByName, name));
            throw new NoContentException();
        }
        LOGGER.info(finished, String.format(findByName, name));
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findAll);
        List<Order> orders = orderDAO.findAll();
        LOGGER.info(finished, findAll);
        return orders;
    }

    public Order update(Order order) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, update);
        Order existedOrder = orderDAO.findById(order.getId());
        existedOrder.setCustomerMail(order.getCustomerMail());
        existedOrder.setDescription(order.getDescription());
        existedOrder = orderDAO.update(existedOrder);
        LOGGER.info(finished, update);
        return existedOrder;
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(delete, id));
        orderDAO.delete(id);
        LOGGER.info(finished, String.format(delete, id));
    }

    @Transactional(readOnly = true)
    public Set<OrderItem> findOrderItems(long orderId) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findOrderItems, orderId));
        Set<OrderItem> orderItems = orderDAO.findOrderItems(orderId);
        LOGGER.info(finished, String.format(findOrderItems, orderId));
        return orderItems;
    }

    public Order addOrderItem(long orderId, OrderItem orderItem) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(addOrderITem, orderId));
        Order order;
        try {
            order = orderDAO.findById(orderId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("There is no order with id " + orderId);
        }
        orderItem.setId(0);
        order.getOrderItems().add(orderItem);
        order = orderDAO.update(order);
        LOGGER.info(finished, String.format(addOrderITem, orderId));
        return order;
    }

    public Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(removeOrderItem, orderId));
        Order order;
        try {
            order = orderDAO.findById(orderId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("There is no order with id " + orderId);
        }
        order = orderDAO.removeOrderItem(orderId, orderItemId);
        LOGGER.info(finished, String.format(removeOrderItem, orderId));
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByPaymentStatus(int paymentStatus) throws StatusSignException, PersistenceMethodException,
            EntityNotFoundException {
        LOGGER.info(started, String.format(findOrdersByPaymentStatus, paymentStatus));
        isValidStatus(paymentStatus);
        List<Order> orders = orderDAO.findOrdersByPaymentStatus(paymentStatus);
        LOGGER.info(finished, String.format(findOrdersByPaymentStatus, paymentStatus));
        return orders;
    }

    public Order updateStatus(long id, int status) throws UpdateStatusException, PersistenceMethodException,
            EntityNotFoundException {
        LOGGER.info(started, String.format(updateStatus, id));
        Order order = null;
        try {
            order = orderDAO.findById(id);
            isValidStatus(order.getPaymentStatus(), status);
            order.setPaymentStatus(status);
            order = orderDAO.update(order);
            LOGGER.info(finished, String.format(updateStatus, id));
            return order;
        } catch (Exception e) {
            if (order != null) {
                order.setPaymentStatus(Statuses.REJECTED_PAYMENT.value());
                update(order);
            }
            throw new UpdateStatusException("Fail to update status of the order with id " + id, e);
        }
    }

    private void isValidStatus(int status) throws StatusSignException {
        if (!Statuses.consists(status)) {
            throw new StatusSignException();
        }
    }

    private void isValidStatus(int currentStatus, int newStatus) throws StatusSignException {
        isValidStatus(newStatus);

        final int OPENED = Statuses.OPENED.value();
        final int PAID = Statuses.PAID.value();
        final int CANCELED = Statuses.CANCELED.value();
        final int REJECTED = Statuses.REJECTED_PAYMENT.value();

        final String OPENED_MESSAGE = "Opened order can be changed either to paid or canceled";
        final String REJECTED_MESSAGE = "Rejected payment can be changed either to paid or canceled";
        final String CANCELED_MESSAGE = "Canceled order can't be changed";
        final String PAID_MESSAGE = "Paid order can't be changed";

        if (currentStatus == OPENED) {
            if (newStatus == OPENED || newStatus == REJECTED) {
                throw new StatusSignException(OPENED_MESSAGE);
            }
        }
        if (currentStatus == REJECTED) {
            if (newStatus == OPENED || newStatus == REJECTED) {
                throw new StatusSignException(REJECTED_MESSAGE);
            }
        }
        if (currentStatus == PAID) {
            throw new StatusSignException(PAID_MESSAGE);
        }
        if (currentStatus == CANCELED) {
            throw new StatusSignException(CANCELED_MESSAGE);
        }
    }

}

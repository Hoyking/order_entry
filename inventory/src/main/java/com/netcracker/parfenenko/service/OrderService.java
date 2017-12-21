package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OrderDAO;
import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderService {

    private OrderDAO orderDAO;

    @Autowired
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Order save(Order order) throws PersistenceMethodException {
        order.setOrderItems(new HashSet<>(0));
        order.setTotalPrice(0);
        order.setOrderDate(new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
        order = orderDAO.save(order);
        order.setName("Order #" + order.getId());
        return update(order);
    }

    @Transactional(readOnly = true)
    public Order findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        return orderDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Order findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return orderDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() throws PersistenceMethodException, EntityNotFoundException {
        return orderDAO.findAll();
    }

    public Order update(Order order) throws PersistenceMethodException, EntityNotFoundException {
        order.setOrderItems(findOrderItems(order.getId()));
        return orderDAO.update(order);
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        orderDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public Set<OrderItem> findOrderItems(long orderId) throws PersistenceMethodException, EntityNotFoundException {
        return orderDAO.findOrderItems(orderId);
    }

    public Order addOrderItem(long orderId, OrderItem orderItem) throws PersistenceMethodException, EntityNotFoundException {
        return orderDAO.addOrderItem(orderId, orderItem);
    }

    public Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException {
        return orderDAO.removeOrderItem(orderId, orderItemId);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByPaymentStatus(int paymentStatus) throws StatusSignException, PersistenceMethodException,
            EntityNotFoundException {
        return orderDAO.findOrdersByPaymentStatus(paymentStatus);
    }

    public Order updateStatus(long id, int status) throws UpdateStatusException, PersistenceMethodException,
            EntityNotFoundException {
        return orderDAO.updateStatus(id, status);
    }

}

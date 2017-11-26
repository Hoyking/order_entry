package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OrderDAO;
import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private OrderDAO orderDAO;

    @Autowired
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Transactional
    public Order save(Order order) {
        return orderDAO.save(order);
    }

    @Transactional
    public Order findById(long id) {
        return orderDAO.findById(id);
    }

    @Transactional
    public Order findByName(String name) {
        return orderDAO.findByName(name);
    }

    @Transactional
    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    @Transactional
    public Order update(Order order) {
        return orderDAO.update(order);
    }

    @Transactional
    public void delete(long id) {
        orderDAO.delete(id);
    }

    @Transactional
    public Order addOrderItem(long orderId, OrderItem orderItem) {
        return orderDAO.addOrderItem(orderId, orderItem);
    }

    @Transactional
    public Order removeOrderItem(long orderId, OrderItem orderItem) {
        return orderDAO.removeOrderItem(orderId, orderItem);
    }

    @Transactional
    public List<Order> findOrdersByPaymentStatus(int paymentStatus) throws PaymentStatusException {
        return orderDAO.findOrdersByPaymentStatus(paymentStatus);
    }

    @Transactional
    public Order payForOrder(long id) throws PayForOrderException {
        return orderDAO.payForOrder(id);
    }

}

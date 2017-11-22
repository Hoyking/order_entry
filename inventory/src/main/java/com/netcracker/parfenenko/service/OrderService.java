package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OrderDAO;
import com.netcracker.parfenenko.entities.InventoryOrder;
import com.netcracker.parfenenko.entities.OrderItem;
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
    public InventoryOrder save(InventoryOrder order) {
        return orderDAO.save(order);
    }

    @Transactional
    public InventoryOrder findById(long id) {
        return orderDAO.findById(id);
    }

    @Transactional
    public InventoryOrder findByName(String name) {
        return orderDAO.findByName(name);
    }

    @Transactional
    public List<InventoryOrder> findAll() {
        return orderDAO.findAll();
    }

    @Transactional
    public InventoryOrder update(InventoryOrder order) {
        return orderDAO.update(order);
    }

    @Transactional
    public void delete(long id) {
        orderDAO.delete(id);
    }

    @Transactional
    public InventoryOrder addOrderItem(long orderId, OrderItem orderItem) {
        return orderDAO.addOrderItem(orderId, orderItem);
    }

    @Transactional
    public InventoryOrder removeOrderItem(long orderId, OrderItem orderItem) {
        return orderDAO.removeOrderItem(orderId, orderItem);
    }

}

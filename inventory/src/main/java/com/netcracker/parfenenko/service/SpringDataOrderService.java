package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import com.netcracker.parfenenko.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpringDataOrderService extends OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public SpringDataOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) throws PersistenceMethodException {
        LOGGER.info(started, save);
        order.setOrderItems(new HashSet<>(0));
        order.setTotalPrice(0);
        order.setOrderDate(new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
        order.setName("temp");
        order = orderRepository.save(order);
        order.setName("Order #" + order.getId());
        order = orderRepository.update(order);
        LOGGER.info(finished, save);
        return order;
    }

    @Override
    public Order findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public Order findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public List<Order> findAll() throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public Order update(Order order) throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {

    }

    @Override
    public Set<OrderItem> findOrderItems(long orderId) throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public Order addOrderItem(long orderId, OrderItem orderItem) throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public List<Order> findOrdersByPaymentStatus(String paymentStatus) throws StatusSignException, PersistenceMethodException, EntityNotFoundException {
        return null;
    }

    @Override
    public Order updateStatus(long id, String status) throws UpdateStatusException, PersistenceMethodException, EntityNotFoundException, StatusSignException {
        return null;
    }

    @Override
    public Order countTotalPrice(long orderId) {
        return null;
    }

}

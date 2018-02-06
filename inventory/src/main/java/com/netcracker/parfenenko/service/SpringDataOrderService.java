package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.exception.DocumentNotFoundException;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import com.netcracker.parfenenko.repository.MongoTemplateOrderRepository;
import com.netcracker.parfenenko.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("Duplicates")
@Service
public class SpringDataOrderService extends OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private OrderRepository orderRepository;

    @Autowired
    public SpringDataOrderService(MongoTemplateOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        LOGGER.info(STARTED, SAVE);
        order.setOrderItems(new HashSet<>(0));
        order.setTotalPrice(0);
        order.setOrderDate(new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
        order.setName("temp");
        order = orderRepository.save(order);
        order.setName("Order #" + order.getId());
        order = orderRepository.update(order);
        LOGGER.info(FINISHED, SAVE);
        return order;
    }

    @Override
    public Order findById(String id) {
        LOGGER.info(STARTED, String.format(FIND_BY_ID, id));
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new NoContentException();
        }
        LOGGER.info(FINISHED, String.format(FIND_BY_ID, id));
        return order;
    }

    @Override
    public Order findByName(String name) {
        LOGGER.info(STARTED, String.format(FIND_BY_NAME, name));
        Order order = orderRepository.findByName(name);
        if (order == null) {
            throw new NoContentException();
        }
        LOGGER.info(FINISHED, String.format(FIND_BY_ID, name));
        return order;
    }

    @Override
    public List<Order> findAll() {
        LOGGER.info(STARTED, FIND_ALL);
        List<Order> orders = orderRepository.findAll();
        LOGGER.info(FINISHED, FIND_ALL);
        return orders;
    }

    @Override
    public Order update(Order order) {
        LOGGER.info(STARTED, UPDATE);
        Order existedOrder = orderRepository.findById(order.getId());
        if (existedOrder == null) {
            throw new DocumentNotFoundException();
        }
        existedOrder.setCustomerMail(order.getCustomerMail());
        existedOrder.setDescription(order.getDescription());
        existedOrder = orderRepository.update(existedOrder);
        LOGGER.info(FINISHED, UPDATE);
        return existedOrder;
    }

    @Override
    public void delete(String id) {
        LOGGER.info(STARTED, String.format(DELETE, id));
        if (orderRepository.findById(id) == null) {
            throw new DocumentNotFoundException();
        }
        orderRepository.delete(id);
        LOGGER.info(FINISHED, String.format(DELETE, id));
    }

    @Override
    public Set<OrderItem> findOrderItems(String orderId) {
        LOGGER.info(STARTED, String.format(FIND_ORDER_ITEMS, orderId));
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new DocumentNotFoundException();
        }
        Set<OrderItem> orderItems = orderRepository.findOrderItems(orderId);
        LOGGER.info(FINISHED, String.format(FIND_ORDER_ITEMS, orderId));
        return orderItems;
    }

    @Override
    public Order addOrderItem(String orderId, OrderItem orderItem) {
        LOGGER.info(STARTED, String.format(ADD_ORDER_ITEM, orderId));
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new DocumentNotFoundException();
        }
        orderItem.setId(UUID.randomUUID().toString());
        order.getOrderItems().add(orderItem);
        order = orderRepository.update(order);
        LOGGER.info(FINISHED, String.format(ADD_ORDER_ITEM, orderId));
        return order;
    }

    @Override
    public Order removeOrderItem(String orderId, String orderItemId) {
        LOGGER.info(STARTED, String.format(REMOVE_ORDER_ITEM, orderId));
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new DocumentNotFoundException();
        }
        order = orderRepository.removeOrderItem(orderId, orderItemId);
        LOGGER.info(FINISHED, String.format(REMOVE_ORDER_ITEM, orderId));
        return order;
    }

    @Override
    public List<Order> findOrdersByPaymentStatus(String paymentStatus) throws StatusSignException {
        LOGGER.info(STARTED, String.format(FIND_ORDERS_BY_PAYMENT_STATUS, paymentStatus));
        isValidStatus(paymentStatus);
        List<Order> orders = orderRepository.findOrdersByPaymentStatus(paymentStatus);
        LOGGER.info(FINISHED, String.format(FIND_ORDERS_BY_PAYMENT_STATUS, paymentStatus));
        return orders;
    }

    @Override
    public Order updateStatus(String id, String status) throws UpdateStatusException, StatusSignException {
        LOGGER.info(STARTED, String.format(UPDATE_STATUS, id));
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new DocumentNotFoundException();
        }
        isValidStatus(order.getPaymentStatus(), status);
        order.setPaymentStatus(status);
        order = orderRepository.update(order);
        LOGGER.info(FINISHED, String.format(UPDATE_STATUS, id));
        return order;
    }

    @Override
    public Order countTotalPrice(String orderId) {
        LOGGER.info(STARTED, String.format(COUNT_TOTAL_PRICE, orderId));
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new DocumentNotFoundException();
        }
        order.setTotalPrice(0);
        for(OrderItem orderItem: orderRepository.findOrderItems(orderId)) {
            order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
        }
        order = orderRepository.update(order);
        LOGGER.info(FINISHED, String.format(COUNT_TOTAL_PRICE, orderId));
        return order;
    }

}

package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.client.OfferClient;
import com.netcracker.parfenenko.client.OrderClient;
import com.netcracker.parfenenko.entity.FreshOrder;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.util.Statuses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private OrderClient orderClient;
    private OfferClient offerClient;

    @Autowired
    public OrderService(OrderClient orderClient, OfferClient offerClient) {
        this.orderClient = orderClient;
        this.offerClient = offerClient;
    }

    public ResponseEntity<Order> createOrder(FreshOrder freshOrder, List<Long> offers) {
        String operation = "creating a new order";
        logger.info("START OPERATION: " + operation);
        Order order = orderClient.createOrder(freshOrder).getBody();
        final long orderId = order.getId();
        offers.forEach(id -> addOrderItem(orderId, id));
        ResponseEntity<Order> response = findOrderById(orderId);
        logger.info("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<Order> findOrderById(long orderId) {
        String operation = String.format("searching for an order by id %s", orderId);
        logger.info("START OPERATION: " + operation);
        ResponseEntity<Order> response = orderClient.findOrderById(orderId);
        logger.error("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<Order> findOrderByName(String name) {
        String operation = "searching for order with name " + name;
        logger.info("START OPERATION: " + operation);
        ResponseEntity<Order> response = orderClient.findOrderByName(name);
        logger.error("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<Order> addOrderItem(long orderId, long offerId) throws UpdateOrderException, EntityNotFoundException {
        String operation = "adding order item based on the offer with id " + offerId +
                " to the order with id " + orderId;
        logger.info("START OPERATION: " + operation);
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Statuses.PAID.value()) {
            throw new UpdateOrderException("Failed to add new order item to the paid order");
        }
        if (order.getPaymentStatus() == Statuses.CANCELED.value()) {
            throw new UpdateOrderException("Fail to add order item to the canceled order");
        }
        Offer offer = offerClient.findOfferById(offerId).getBody();
        if (offer == null) {
            throw new EntityNotFoundException("Can't find Offer entity with id " + offerId);
        }
        orderClient.addOrderItem(orderId, convertFromOffer(offer));
        ResponseEntity<Order> response = countTotalPrice(orderId);
        logger.info("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<Order> removeOrderItem(long orderId, long orderItemId) throws UpdateOrderException {
        String operation = "removing order item with id " + orderItemId +
                " from the order with id " + orderId;
        logger.info("START OPERATION: " + operation);
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Statuses.PAID.value()) {
            throw new UpdateOrderException("Fail to remove order item from the paid order");
        }
        if (order.getPaymentStatus() == Statuses.CANCELED.value()) {
            throw new UpdateOrderException("Fail to remove order item from the canceled order");
        }
        orderClient.removeOrderItem(orderId, orderItemId);
        ResponseEntity<Order> response = countTotalPrice(orderId);
        logger.info("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<Order[]> findAll() {
        String operation = "searching for all orders";
        logger.info("START OPERATION: " + operation);
        ResponseEntity<Order[]> response = orderClient.findAll();
        logger.error("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<Order[]> findOrderByStatus(int status) {
        String operation = "searching for orders by payment status " + status;
        logger.info("START OPERATION: " + operation);
        ResponseEntity<Order[]> response = orderClient.findOrderByStatus(status);
        logger.error("END OF OPERATION: " + operation);
        return response;
    }

    private ResponseEntity<Order> countTotalPrice(long orderId) throws EntityNotFoundException {
        String operation = "counting total price of the order with id " + orderId;
        logger.info("START OPERATION: " + operation);
        Order order = findOrderById(orderId).getBody();
        try {
            order.setTotalPrice(0);
        } catch (NullPointerException e) {
            throw new EntityNotFoundException("There is no order with such id");
        }
        for(OrderItem orderItem: findOrderItems(order.getId()).getBody()) {
            order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
        }
        return orderClient.updateOrder(order);
    }

    public ResponseEntity<Order> updateStatus(long orderId, int status) {
        String operation = "changing status to " + status + " in the order with id " + orderId;
        logger.info("START OPERATION: " + operation);
        ResponseEntity<Order> response = orderClient.updateStatus(orderId, status);
        logger.error("END OF OPERATION: " + operation);
        return response;
    }

    public ResponseEntity<OrderItem[]> findOrderItems(long orderId) {
        String operation = "searching for order items of the order with id " + orderId;
        logger.info("START OPERATION: " + operation);
        ResponseEntity<OrderItem[]> response = orderClient.findOrderItems(orderId);
        logger.error("END OF OPERATION: " + operation);
        return response;
    }

    private OrderItem convertFromOffer(Offer offer) {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(offer.getName());
        orderItem.setDescription(offer.getDescription());
        orderItem.setCategory(offer.getCategory().getName());
        orderItem.setPrice(offer.getPrice().getValue());
        return orderItem;
    }

}

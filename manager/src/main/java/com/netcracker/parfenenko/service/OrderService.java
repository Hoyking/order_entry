package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.client.OrderClient;
import com.netcracker.parfenenko.entity.FreshOrder;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.util.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderService {

    private OrderClient orderClient;
    private OfferService offerService;

    @Autowired
    public OrderService(OrderClient orderClient, OfferService offerService) {
        this.orderClient = orderClient;
        this.offerService = offerService;
    }

    public ResponseEntity<Order> createOrder(FreshOrder freshOrder, List<Long> offers) {
        Order order = orderClient.createOrder(freshOrder).getBody();
        final long orderId = order.getId();
        offers.forEach(id -> {
            try {
                addOrderItem(orderId, id);
            } catch (UpdateOrderException e) {
                e.printStackTrace();
            }
        });
        return findOrderById(orderId);
    }

    public ResponseEntity<Order> findOrderById(long orderId) {
        return orderClient.findOrderById(orderId);
    }

    public ResponseEntity<Order> findOrderByName(String name) {
        return orderClient.findOrderByName(name);
    }

    public ResponseEntity<Order> addOrderItem(long orderId, long offerId) throws UpdateOrderException, EntityNotFoundException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Failed to add new order item to the paid order");
        }
        Offer offer = offerService.findOfferById(offerId).getBody();
        if (offer == null) {
            throw new EntityNotFoundException("Can't find Offer entity with id " + offerId);
        }
        orderClient.addOrderItem(orderId, convertFromOffer(offer));
        return countTotalPrice(orderId);
    }

    public ResponseEntity<Order> removeOrderItem(long orderId, long orderItemId) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to remove order item from the paid order");
        }
        orderClient.removeOrderItem(orderId, orderItemId);
        return countTotalPrice(orderId);
    }

    public ResponseEntity<Order[]> findAll() {
        return orderClient.findAll();
    }

    public ResponseEntity<Order[]> findOrderByStatus(int status) {
        return orderClient.findOrderByStatus(status);
    }

    public ResponseEntity<Order> countTotalPrice(long orderId) throws EntityNotFoundException {
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

    public ResponseEntity<Order> payForOrder(long orderId) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to pay an order. Order is already paid");
        }
        return orderClient.payForOrder(orderId);
    }

    public ResponseEntity<OrderItem[]> findOrderItems(long orderId) {
        return orderClient.findOrderItems(orderId);
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

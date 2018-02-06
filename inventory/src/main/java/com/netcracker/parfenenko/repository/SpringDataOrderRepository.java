package com.netcracker.parfenenko.repository;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SpringDataOrderRepository extends MongoRepository<Order, String> {

    Order findById(String id);

    Order findByName(String name);

    List<Order> findOrdersByPaymentStatus(String paymentStatus);

    default Set<OrderItem> findOrderItems(String id) {
        return findById(id).getOrderItems();
    }

    default Order removeOrderItem(String orderId, String orderItemId) {
        Order order = findById(orderId);
        Optional<OrderItem> orderItemOptional = order.getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getId().equals(orderItemId))
                .findFirst();
        orderItemOptional.ifPresent(orderItem -> order.getOrderItems().remove(orderItem));
        return order;
    }

}

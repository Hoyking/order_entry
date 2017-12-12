package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.util.RequestManager;
import com.netcracker.parfenenko.util.UriProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderClient {

    private RequestManager requestManager;
    private UriProvider uriProvider;

    @Autowired
    public OrderClient(RequestManager requestManager, UriProvider uriProvider) {
        this.requestManager = requestManager;
        this.uriProvider = uriProvider;
    }

    public ResponseEntity<Order> createOrder(Order order) {
        return requestManager.postRequest(uriProvider.get("baseOrdersURI"), new HttpEntity<>(order),
                Order.class);
    }

    public ResponseEntity<Order> findOrderById(long orderId) {
        return requestManager.getRequest(String.format(uriProvider.get("findOrderByIdURI"), orderId),
                Order.class);
    }

    public ResponseEntity<Order> findOrderByName(String name) {
        return requestManager.getRequest(String.format(uriProvider.get("findOrderByNameURI"), name),
                Order.class);
    }

    public ResponseEntity<Order[]> findAll() {
        return requestManager.getRequest(uriProvider.get("baseOrdersURI"), Order[].class);
    }

    public ResponseEntity<Order> updateOrder(Order order) {
        return requestManager.putRequest(uriProvider.get("baseOrdersURI"),
                new HttpEntity<>(order), Order.class);
    }

    public ResponseEntity<Order[]> findOrderByStatus(int status) {
        return requestManager.getRequest(String.format(uriProvider.get("findOrdersByStatusURI"), status),
                Order[].class);
    }

    public ResponseEntity<Order> addOrderItem(long orderId, OrderItem orderItem) {
        return requestManager.postRequest(String.format(uriProvider.get("orderItemURI"), orderId),
                new HttpEntity<>(orderItem), Order.class);
    }

    public ResponseEntity<Order> removeOrderItem(long orderId, long orderItemId) {
        return requestManager.deleteRequest(String.format(uriProvider.get("orderItemURI"), orderId),
                new HttpEntity<>(orderItemId), Order.class);
    }

    public ResponseEntity<Order> payForOrder(long orderId) {
        return requestManager.putRequest(String.format(uriProvider.get("payURI"), orderId), Order.class);
    }

}

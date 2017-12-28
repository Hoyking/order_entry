package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.entity.FreshOrder;
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

    private final String URN = "inventory";

    @Autowired
    public OrderClient(RequestManager requestManager, UriProvider uriProvider) {
        this.requestManager = requestManager;
        this.uriProvider = uriProvider;
    }

    public ResponseEntity<Order> createOrder(FreshOrder order) {
        return requestManager.postRequest(uriProvider.get(URN, "orders"), new HttpEntity<>(order),
                Order.class);
    }

    public ResponseEntity<Order> findOrderById(long orderId) {
        return requestManager.getRequest(String.format(uriProvider.get(URN, "orders/%s"), orderId),
                Order.class);
    }

    public ResponseEntity<Order> findOrderByName(String name) {
        return requestManager.getRequest(String.format(uriProvider.get(URN, "orders?name=%s"), name),
                Order.class);
    }

    public ResponseEntity<Order[]> findAll() {
        return requestManager.getRequest(uriProvider.get(URN, "orders"), Order[].class);
    }

    public ResponseEntity<Order> updateOrder(Order order) {
        return requestManager.putRequest(uriProvider.get(URN, "orders"), new HttpEntity<>(order),
                Order.class);
    }

    public ResponseEntity<Order[]> findOrderByStatus(int status) {
        return requestManager.getRequest(String.format(uriProvider.get(URN, "orders?status=%s"), status),
                Order[].class);
    }

    public ResponseEntity<Order> addOrderItem(long orderId, OrderItem orderItem) {
        return requestManager.postRequest(String.format(uriProvider.get(URN, "orders/%s/orderItem"), orderId),
                new HttpEntity<>(orderItem), Order.class);
    }

    public ResponseEntity<Order> removeOrderItem(long orderId, long orderItemId) {
        return requestManager.deleteRequest(String.format(uriProvider.get(URN, "orders/%s/orderItem"), orderId),
                new HttpEntity<>(orderItemId), Order.class);
    }

    public ResponseEntity<Order> updateStatus(long orderId, String status) {
        return requestManager.putRequest(String.format(uriProvider.get(URN, "orders/%s/status"), orderId),
                new HttpEntity<>(status), Order.class);
    }

    public ResponseEntity<OrderItem[]> findOrderItems(long orderId) {
        return requestManager.getRequest(String.format(uriProvider.get(URN, "orders/%s/orderItems"), orderId),
                OrderItem[].class);
    }

}

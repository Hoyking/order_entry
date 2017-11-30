package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.model.*;
import com.netcracker.parfenenko.util.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class OrderClient {

    private final String OFFERS_WITH_TAGS_URI = "http://localhost:8081/api/v1/offers/tags";
    private final String CATEGORY_OFFERS_URI = "http://localhost:8081/api/v1/categories/%d/offers";
    private final String OFFERS_URI = "http://localhost:8081/api/v1/offers";
    private final String OFFERS_WITH_PRICE_URI = "http://localhost:8081/api/v1/offers/price";
    private final String BASE_ORDERS_URI = "http://localhost:8082/api/v1/orders";
    private final String FIND_ORDER_BY_ID_URI = "http://localhost:8082/api/v1/orders/%s";
    private final String FIND_ORDER_BY_NAME_URI = "http://localhost:8082/api/v1/orders/name/%s";
    private final String ORDER_ITEM_URI = "http://localhost:8082/api/v1/orders/%s/orderItem";
    private final String FIND_ORDERS_BY_STATUS_URI = "http://localhost:8082/api/v1/orders/status/%s";
    private final String PAY_URI = "http://localhost:8082/api/v1/orders/%s/status";

    private RestTemplate restTemplate;

    @Autowired
    public OrderClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<List<Offer>> findOffers(List<Tag> tags, List<Category> categories, double fromPrice, double toPrice) {
        List<Offer> offers = new ArrayList<>();

        if (categories.size() == 0) {
            ResponseEntity<Offer[]> categoryResponseEntity = getRequest(OFFERS_URI,
                    Offer[].class);
            offers.addAll(Arrays.asList(categoryResponseEntity.getBody()));
        } else {
            for(Category category: categories) {
                ResponseEntity<Offer[]> categoryResponseEntity = getRequest(String.format(CATEGORY_OFFERS_URI, category.getId()),
                        Offer[].class);
                offers.addAll(Arrays.asList(categoryResponseEntity.getBody()));
            }
        }

        ResponseEntity<Offer[]> tagResponseEntity = getRequest(OFFERS_WITH_TAGS_URI, new HttpEntity<>(tags), Offer[].class);
        offers.retainAll(Arrays.asList(tagResponseEntity.getBody()));

        Map<String, Double> priceMap = new HashMap<>();
        priceMap.put("from_price", fromPrice);
        priceMap.put("to_price", toPrice);
        ResponseEntity<Offer[]> priceResponseEntity = getRequest(OFFERS_WITH_PRICE_URI, new HttpEntity<>(priceMap),
                Offer[].class);
        offers.retainAll(Arrays.asList(priceResponseEntity.getBody()));

        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    public ResponseEntity<Order> createOrder(Order order) {
        return postRequest(BASE_ORDERS_URI, new HttpEntity<>(order), Order.class);
    }

    public ResponseEntity<Order> findOrderById(long orderId) {
        return getRequest(String.format(FIND_ORDER_BY_ID_URI, orderId), Order.class);
    }

    public ResponseEntity<Order> findOrderByName(String name) {
        return getRequest(String.format(FIND_ORDER_BY_NAME_URI, name), Order.class);
    }

    public ResponseEntity<Order> addOrderItem(long orderId, OrderItem orderItem) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to add new order item to the paid order");
        }
        return postRequest(String.format(ORDER_ITEM_URI, orderId), new HttpEntity<>(orderItem), Order.class);
    }

    public ResponseEntity<Order> removeOrderItem(long orderId, OrderItem orderItem) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to remove order item from the paid order");
        }
        return deleteRequest(String.format(ORDER_ITEM_URI, orderId), new HttpEntity<>(orderItem), Order.class);
    }

    public ResponseEntity<Order[]> findAll() {
        return getRequest(BASE_ORDERS_URI, Order[].class);
    }

    public ResponseEntity<Order[]> findByStatus(int status) {
        return getRequest(String.format(FIND_ORDERS_BY_STATUS_URI, status), Order[].class);
    }

    public ResponseEntity<Order> countTotalPrice(long orderId) {
        Order order = findOrderById(orderId).getBody();
        order.setTotalPrice(0);
        for(OrderItem orderItem: order.getOrderItems()) {
            order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
        }
        return putRequest(BASE_ORDERS_URI, new HttpEntity<>(order), Order.class);
    }

    public ResponseEntity<Order> payForOrder(long orderId) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to pay for order. Order is already paid");
        }
        return putRequest(String.format(PAY_URI, orderId), Order.class);
    }

    private <T> ResponseEntity<T> getRequest(String uri, Class<T> responseType) {
        return restTemplate.getForEntity(uri, responseType);
    }

    private <T, R> ResponseEntity<T> getRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, responseType);
    }

    private <T, R> ResponseEntity<T> postRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.postForEntity(uri, httpEntity, responseType);
    }

    private <T, R> ResponseEntity<T> deleteRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, responseType);
    }

    private <T, R> ResponseEntity<T> putRequest(String uri, HttpEntity<R> httpEntity, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, responseType);
    }

    private <T> ResponseEntity<T> putRequest(String uri, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.PUT, null, responseType);
    }

}

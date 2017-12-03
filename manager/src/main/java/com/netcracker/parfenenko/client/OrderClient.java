package com.netcracker.parfenenko.client;

import com.netcracker.parfenenko.exception.EntityNotFoundException;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.entity.*;
import com.netcracker.parfenenko.filter.OfferFilter;
import com.netcracker.parfenenko.util.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderClient {

    private final String BASE_OFFER_URI = "http://localhost:8081/api/v1/offers/%s";
    private final String OFFERS_WITH_TAGS_URI = "http://localhost:8081/api/v1/offers/tags";
    private final String CATEGORY_OFFERS_URI = "http://localhost:8081/api/v1/categories/%d/offers";
    private final String OFFERS_URI = "http://localhost:8081/api/v1/offers";
    private final String OFFERS_WITH_PRICE_URI = "http://localhost:8081/api/v1/offers/price?from=%s&to=%s";
    private final String BASE_ORDERS_URI = "http://localhost:8082/api/v1/orders";
    private final String FIND_ORDER_BY_ID_URI = "http://localhost:8082/api/v1/orders/%s";
    private final String FIND_ORDER_BY_NAME_URI = "http://localhost:8082/api/v1/orders/name/%s";
    private final String ORDER_ITEM_URI = "http://localhost:8082/api/v1/orders/%s/orderItem";
    private final String FIND_ORDERS_BY_STATUS_URI = "http://localhost:8082/api/v1/orders/status/%s";
    private final String PAY_URI = "http://localhost:8082/api/v1/orders/%s/status";

    private ApplicationContext context;
    private RestTemplate restTemplate;

    @Autowired
    public OrderClient(RestTemplate restTemplate, ApplicationContext context) {
        this.restTemplate = restTemplate;
        this.context = context;
    }

    public ResponseEntity<List<Offer>> findOffers(OfferFilter offerFilter) {
        List<Offer> offers = new ArrayList<>();

        if (offerFilter.getCategories().size() == 0) {
            ResponseEntity<Offer[]> categoryResponseEntity = getRequest(OFFERS_URI,
                    Offer[].class);
            offers.addAll(Arrays.asList(categoryResponseEntity.getBody()));
        } else {
            for(Long categoryId: offerFilter.getCategories()) {
                ResponseEntity<Offer[]> categoryResponseEntity = getRequest(String.format(CATEGORY_OFFERS_URI, categoryId),
                        Offer[].class);
                offers.addAll(Arrays.asList(categoryResponseEntity.getBody()));
            }
        }

        List<Tag> tags = offerFilter.getTags().stream().map(this::createTag).collect(Collectors.toList());

        ResponseEntity<Offer[]> tagResponseEntity = postRequest(OFFERS_WITH_TAGS_URI,
                new HttpEntity<>(tags), Offer[].class);
        offers.retainAll(Arrays.asList(tagResponseEntity.getBody()));

        ResponseEntity<Offer[]> priceResponseEntity = getRequest(String.format(OFFERS_WITH_PRICE_URI,
                offerFilter.getFrom(), offerFilter.getTo()), Offer[].class);
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

    public ResponseEntity<Order> addOrderItem(long orderId, long offerId) throws UpdateOrderException, EntityNotFoundException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to add new order item to the paid order");
        }
        Offer offer = getRequest(String.format(BASE_OFFER_URI, offerId), Offer.class).getBody();
        if (offer == null) {
            throw new EntityNotFoundException("Can't find Offer entity with id " + offerId);
        }
        return postRequest(String.format(ORDER_ITEM_URI, orderId), new HttpEntity<>(convertFromOffer(offer)), Order.class);
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

    private Tag createTag(String tagName) {
        Tag tag = context.getBean(Tag.class);
        tag.setName(tagName);
        return tag;
    }

    private OrderItem convertFromOffer(Offer offer) {
        OrderItem orderItem = context.getBean(OrderItem.class);
        orderItem.setName(offer.getName());
        orderItem.setDescription(offer.getDescription());
        orderItem.setCategory(offer.getCategory().getName());
        orderItem.setTags(offer.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        orderItem.setPrice(offer.getPrice().getValue());
        return orderItem;
    }

    private <T> ResponseEntity<T> getRequest(String uri, Class<T> responseType) {
        return restTemplate.getForEntity(uri, responseType);
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

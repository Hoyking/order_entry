package com.netcracker.parfenenko.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.entity.Tag;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.util.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderClient {

    private Map<String, String> uriMap;
    private RestTemplate restTemplate;

    @Autowired
    public OrderClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        loadURIs();
    }

    private void loadURIs() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            uriMap = mapper.readValue(new File("manager/src/main/resources/uri.yml"), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<List<Offer>> findOffers(List<Long> categories, List<String> tags, double from, double to) {
        List<Offer> offers = new ArrayList<>();

        if (categories.size() == 0) {
            ResponseEntity<Offer[]> categoryResponseEntity = getRequest(uriMap.get("offersURI"),
                    Offer[].class);
            offers.addAll(Arrays.asList(categoryResponseEntity.getBody()));
        } else {
            for(Long categoryId: categories) {
                ResponseEntity<Offer[]> categoryResponseEntity = getRequest(
                        String.format(uriMap.get("categoryOffersURI"), categoryId), Offer[].class);
                offers.addAll(Arrays.asList(categoryResponseEntity.getBody()));
            }
        }

        StringBuilder builder = new StringBuilder();
        tags.forEach(tag -> builder.append(tag).append(","));
        if (builder.length() != 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        ResponseEntity<Offer[]> tagResponseEntity = getRequest(String.format(uriMap.get("offersWithTagsURI"), builder.toString()),
                Offer[].class);
        offers.retainAll(Arrays.asList(tagResponseEntity.getBody()));

        ResponseEntity<Offer[]> priceResponseEntity = getRequest(String.format(uriMap.get("offersWithPriceURI"),
                from, to), Offer[].class);
        offers.retainAll(Arrays.asList(priceResponseEntity.getBody()));

        if (offers.size() == 0) {
            return new ResponseEntity<>(offers, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(offers, HttpStatus.OK);
        }
    }

    public ResponseEntity<Order> createOrder(Order order) {
        return postRequest(uriMap.get("baseOrdersURI"), new HttpEntity<>(order), Order.class);
    }

    public ResponseEntity<Order> findOrderById(long orderId) {
        return getRequest(String.format(uriMap.get("findOrderByIdURI"), orderId), Order.class);
    }

    public ResponseEntity<Order> findOrderByName(String name) {
        return getRequest(String.format(uriMap.get("findOrderByNameURI"), name), Order.class);
    }

    public ResponseEntity<Order> addOrderItem(long orderId, long offerId) throws UpdateOrderException, EntityNotFoundException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Failed to add new order item to the paid order");
        }
        Offer offer = getRequest(String.format(uriMap.get("baseOfferURI"), offerId), Offer.class).getBody();
        if (offer == null) {
            throw new EntityNotFoundException("Can't find Offer entity with id " + offerId);
        }
        postRequest(String.format(uriMap.get("orderItemURI"), orderId), new HttpEntity<>(convertFromOffer(offer)), Order.class);
        return payForOrder(orderId);
    }

    public ResponseEntity<Order> removeOrderItem(long orderId, long orderItemId) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to remove order item from the paid order");
        }
        deleteRequest(String.format(uriMap.get("orderItemURI"), orderId), new HttpEntity<>(orderItemId), Order.class);
        return payForOrder(orderId);
    }

    public ResponseEntity<Order[]> findAll() {
        return getRequest(uriMap.get("baseOrdersURI"), Order[].class);
    }

    public ResponseEntity<Order[]> findByStatus(int status) {
        return getRequest(String.format(uriMap.get("findOrdersByStatusURI"), status), Order[].class);
    }

    public ResponseEntity<Order> countTotalPrice(long orderId) throws EntityNotFoundException {
        Order order = findOrderById(orderId).getBody();
        try {
            order.setTotalPrice(0);
        } catch (NullPointerException e) {
            throw new EntityNotFoundException("There is no order with such id");
        }
        for(OrderItem orderItem: order.getOrderItems()) {
            order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
        }
        return putRequest(uriMap.get("baseOrdersURI"), new HttpEntity<>(order), Order.class);
    }

    public ResponseEntity<Order> payForOrder(long orderId) throws UpdateOrderException {
        Order order = findOrderById(orderId).getBody();
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new UpdateOrderException("Fail to pay for order. Order is already paid");
        }
        return putRequest(String.format(uriMap.get("payURI"), orderId), Order.class);
    }

    private OrderItem convertFromOffer(Offer offer) {
        OrderItem orderItem = new OrderItem();
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

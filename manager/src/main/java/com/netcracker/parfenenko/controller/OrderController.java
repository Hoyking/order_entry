package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.client.OrderClient;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.exception.EntityNotFoundException;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class OrderController {

    private OrderClient orderClient;

    @Autowired
    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    public ResponseEntity findOffers(@RequestParam(name = "categories") List<Long> categories,
            @RequestParam(name = "tags") List<String> tags,
            @RequestParam(name = "from") double from,
            @RequestParam(name = "to") double to) {
        return orderClient.findOffers(categories, tags, from, to);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity createOrder(@RequestBody Order order) {
        return orderClient.createOrder(order);
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity findOrderById(@PathVariable long id) {
        return orderClient.findOrderById(id);
    }

    @RequestMapping(value = "/orders/name/{name}", method = RequestMethod.GET)
    public ResponseEntity findOrderByName(@PathVariable String name) {
        return orderClient.findOrderByName(name);
    }

    @RequestMapping(value = "/orders/{id}/orderItem", method = RequestMethod.POST)
    public ResponseEntity addOrderItem(@PathVariable long id, @RequestBody long offerId) {
        try {
            return orderClient.addOrderItem(id, offerId);
        } catch (UpdateOrderException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/orders/{id}/orderItem", method = RequestMethod.DELETE)
    public ResponseEntity removeOrderItem(@PathVariable long id, @RequestBody long orderItemId) {
        try {
            return orderClient.removeOrderItem(id, orderItemId);
        } catch (UpdateOrderException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity findAllOrders() {
        return orderClient.findAll();
    }

    @RequestMapping(value = "/orders/status/{status}", method = RequestMethod.GET)
    public ResponseEntity findOrdersByStatus(@PathVariable int status) {
        return orderClient.findByStatus(status);
    }

    @RequestMapping(value = "/orders/{id}/price", method = RequestMethod.PUT)
    public ResponseEntity countTotalPrice(@PathVariable long id) {
        return orderClient.countTotalPrice(id);
    }

    @RequestMapping(value = "/orders/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity payForOrder(@PathVariable long id) {
        try {
            return orderClient.payForOrder(id);
        } catch (UpdateOrderException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

}

package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.client.OrderClient;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.model.Category;
import com.netcracker.parfenenko.model.Order;
import com.netcracker.parfenenko.model.OrderItem;
import com.netcracker.parfenenko.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class Controller {

    private OrderClient orderClient;

    @Autowired
    public Controller(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    public ResponseEntity findOffers(@RequestParam(name = "tags") List<Tag> tags,
                                     @RequestParam(name = "categories") List<Category> categories,
                                     @RequestParam(name = "fromPrice") double fromPrice,
                                     @RequestParam(name = "toPrice") double toPrice) {
        return orderClient.findOffers(tags, categories, fromPrice, toPrice);
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
    public ResponseEntity addOrderItem(@PathVariable long id, @RequestBody OrderItem orderItem) {
        try {
            return orderClient.addOrderItem(id, orderItem);
        } catch (UpdateOrderException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/orders/{id}/orderItem", method = RequestMethod.DELETE)
    public ResponseEntity removeOrderItem(@PathVariable long id, @RequestBody OrderItem orderItem) {
        try {
            return orderClient.removeOrderItem(id, orderItem);
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
        try {
            return orderClient.payForOrder(id);
        } catch (UpdateOrderException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

}

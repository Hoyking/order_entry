package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.save(order), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderById(@PathVariable long id) {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderByName(@PathVariable String name) {
        return new ResponseEntity<>(orderService.findByName(name), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Order>> findAllOrders() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.update(order), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Order> deleteOrder(@PathVariable long id) {
        Order order = null;
        orderService.delete(id);
        return new ResponseEntity<>(order, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/order_item", method = RequestMethod.PUT)
    public ResponseEntity<Order> addOrderItemToOrder(@PathVariable long id, @RequestBody OrderItem orderItem) {
        return new ResponseEntity<>(orderService.addOrderItem(id, orderItem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/order_item", method = RequestMethod.DELETE)
    public ResponseEntity<Order> removeOrderItemFromOrder(@PathVariable long id, @RequestBody OrderItem orderItem) {
        return new ResponseEntity<>(orderService.removeOrderItem(id, orderItem), HttpStatus.OK);
    }

}

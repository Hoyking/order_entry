package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import com.netcracker.parfenenko.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/orders")
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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Order> deleteOrder(@PathVariable long id) {
        Order order = null;
        orderService.delete(id);
        return new ResponseEntity<>(order, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/orderItems", method = RequestMethod.GET)
    public ResponseEntity<Set<OrderItem>> findOrderItemsOfOrder(@PathVariable long orderId) {
        return new ResponseEntity<>(orderService.findOrderItems(orderId), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/orderItem", method = RequestMethod.POST)
    public ResponseEntity<Order> addOrderItemToOrder(@PathVariable long id, @RequestBody OrderItem orderItem) {
        return new ResponseEntity<>(orderService.addOrderItem(id, orderItem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/orderItem", method = RequestMethod.DELETE)
    public ResponseEntity<Order> removeOrderItemFromOrder(@PathVariable long id, @RequestBody long orderItemId) {
        return new ResponseEntity<>(orderService.removeOrderItem(id, orderItemId), HttpStatus.OK);
    }

    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> findOrdersByPaymentStatus(@PathVariable int status) {
        List<Order> orders = null;
        try {
            orders = orderService.findOrdersByPaymentStatus(status);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (PaymentStatusException e) {
            e.printStackTrace();
            return new ResponseEntity<>(orders, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<Order> payForOrder(@PathVariable long id) {
        try {
            return new ResponseEntity<>(orderService.payForOrder(id), HttpStatus.OK);
        } catch (PayForOrderException e) {
            e.printStackTrace();
            Order nullOrder = null;
            return new ResponseEntity<>(nullOrder, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

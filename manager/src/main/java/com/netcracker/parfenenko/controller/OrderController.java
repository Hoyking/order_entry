package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.client.OrderClient;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.exception.UpdateOrderException;
import com.netcracker.parfenenko.filter.OfferFilter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class OrderController {

    private OrderClient orderClient;

    @Autowired
    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers with filters",
            response = Order.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 204, message = "There are no offers with such filters")
    })
    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    public ResponseEntity<Offer[]> findOffers(@ModelAttribute(name = "filters") OfferFilter offerFilter) {
        return orderClient.findOffers(offerFilter);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new order",
            response = Order.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Order> createOrder(@RequestBody Order order,
            @RequestParam(name = "offers", required = false, defaultValue = "") List<Long> offers) {
        return orderClient.createOrder(order, offers);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by id",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There is no order with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderById(@PathVariable long id) {
        return orderClient.findOrderById(id);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by name",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There is no order with such name")
    })
    @RequestMapping(value = "/orders/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderByName(@PathVariable String name) {
        return orderClient.findOrderByName(name);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Adding order item to existing order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Either order or offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 409, message = "Failed to add new order item to the paid order")
    })
    @RequestMapping(value = "/orders/{id}/orderItem", method = RequestMethod.POST)
    public ResponseEntity<Order> addOrderItem(@PathVariable long id, @RequestBody long offerId) {
        try {
            return orderClient.addOrderItem(id, offerId);
        } catch (UpdateOrderException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Removing order item from existing order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Either order or order item doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 409, message = "Failed to remove order item from the paid order")
    })
    @RequestMapping(value = "/orders/{id}/orderItem", method = RequestMethod.DELETE)
    public ResponseEntity<Order> removeOrderItem(@PathVariable long id, @RequestBody long orderItemId) {
        try {
            return orderClient.removeOrderItem(id, orderItemId);
        } catch (UpdateOrderException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all offers",
            response = Order[].class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no existing orders")
    })
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<Order[]> findAllOrders() {
        return orderClient.findAll();
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for orders with requested payment status",
            response = Order[].class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There are no orders with requested payment status"),
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong payment status value")
    })
    @RequestMapping(value = "/orders/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<Order[]> findOrdersByStatus(@PathVariable int status) {
        return orderClient.findByStatus(status);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Counting total price of the order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist")
    })
    @RequestMapping(value = "/orders/{id}/price", method = RequestMethod.PUT)
    public ResponseEntity<Order> countTotalPrice(@PathVariable long id) {
        try {
            return orderClient.countTotalPrice(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Payment for the order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 409, message = "The order is already paid"),
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "Order doesn't exist")
    })
    @RequestMapping(value = "/orders/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<Order> payForOrder(@PathVariable long id) {
        try {
            return orderClient.payForOrder(id);
        } catch (UpdateOrderException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}

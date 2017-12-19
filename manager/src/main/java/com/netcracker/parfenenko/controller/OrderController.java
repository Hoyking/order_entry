package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entity.FreshOrder;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.service.OfferService;
import com.netcracker.parfenenko.service.OrderService;
import com.netcracker.parfenenko.util.Statuses;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class OrderController {

    private OrderService orderService;
    private OfferService offerService;

    @Autowired
    public OrderController(OrderService orderService, OfferService offerService) {
        this.orderService = orderService;
        this.offerService = offerService;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Searching for offers with filters",
            response = Order.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong filters")
    })
    @RequestMapping(value = "/offers", method = RequestMethod.POST)
    public ResponseEntity<Offer[]> findOffers(@RequestBody Map<String, List<String>> offerFilter) {
        return offerService.findOffers(offerFilter);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new order",
            response = Order.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Order> createOrder(@RequestBody FreshOrder order,
            @RequestParam(name = "offers", required = false, defaultValue = "") List<Long> offers) {
        return orderService.createOrder(order, offers);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by id",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is no order with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderById(@PathVariable long id) {
        return orderService.findOrderById(id);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by name",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "There is no order with such name")
    })
    @RequestMapping(value = "/orders/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderByName(@PathVariable String name) {
        return orderService.findOrderByName(name);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching order items of an order",
            response = OrderItem[].class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "There is no order with such name")
    })
    @RequestMapping(value = "/orders/{id}/orderItems", method = RequestMethod.GET)
    public ResponseEntity<OrderItem[]> findOrderItemsOfOrder(@PathVariable long id) {
        return orderService.findOrderItems(id);
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
        return orderService.addOrderItem(id, offerId);
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
        return orderService.removeOrderItem(id, orderItemId);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all orders",
            response = Order[].class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<Order[]> findAllOrders() {
        return orderService.findAll();
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for orders with requested payment status",
            response = Order[].class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong payment status value")
    })
    @RequestMapping(value = "/orders/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<Order[]> findOrdersByStatus(@PathVariable int status) {
        return orderService.findOrderByStatus(status);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Counting total price of the order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "Order doesn't exist")
    })
    @RequestMapping(value = "/orders/{id}/price", method = RequestMethod.PUT)
    public ResponseEntity<Order> countTotalPrice(@PathVariable long id) {
        return orderService.countTotalPrice(id);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Order payment",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 400, message = "Invalid payment status")
    })
    @RequestMapping(value = "/orders/{id}/pay", method = RequestMethod.PUT)
    public ResponseEntity<Order> payForOrder(@PathVariable long id) {
        return orderService.updateStatus(id, Statuses.PAID.value());
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Cancel order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 400, message = "Invalid payment status")
    })
    @RequestMapping(value = "/orders/{id}/cancel", method = RequestMethod.PUT)
    public ResponseEntity<Order> cancelOrder(@PathVariable long id) {
        return orderService.updateStatus(id, Statuses.CANCELED.value());
    }

}

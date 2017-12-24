package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entity.*;
import com.netcracker.parfenenko.service.CategoryService;
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
public class ServiceController {

    private OrderService orderService;
    private OfferService offerService;
    private CategoryService categoryService;

    @Autowired
    public ServiceController(OrderService orderService, OfferService offerService, CategoryService categoryService) {
        this.orderService = orderService;
        this.offerService = offerService;
        this.categoryService = categoryService;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Searching for offers with filters",
            response = Offer[].class
    )
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong filters")
    })
    @RequestMapping(value = "/offers", method = RequestMethod.POST)
    public ResponseEntity<Offer[]> findOffers(@RequestBody Map<String, List<String>> offerFilter) {
        return offerService.findOffers(offerFilter);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for offers by part of name",
            response = Offer[].class
    )
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/offers", params = {"namePart"}, method = RequestMethod.GET)
    public ResponseEntity<Offer[]> findOffersByPartOfName(@RequestParam(name = "namePart") String namePart) {
        return offerService.findOffersByPartOfName(namePart);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for categories",
            response = Category[].class
    )
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Category[]> findCategories() {
        return categoryService.findAll();
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
    @RequestMapping(value = "/orders", params = {"name"}, method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderByName(@RequestParam(name = "name") String name) {
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
    @RequestMapping(value = "/orders", params = {"status"}, method = RequestMethod.GET)
    public ResponseEntity<Order[]> findOrdersByStatus(@RequestParam(name = "status") int status) {
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

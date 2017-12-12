package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new order",
            response = Order.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> saveOrder(@RequestBody Order order) {
        try {
            return new ResponseEntity<>(orderService.save(order), HttpStatus.CREATED);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by id",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 204, message = "There is no order with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by name",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There is no order with such name")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<Order> findOrderByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(orderService.findByName(name), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all offers",
            response = Order.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 204, message = "There are no existing orders")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Order>> findAllOrders() {
        try {
            return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing offer",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        try {
            return new ResponseEntity<>(orderService.update(order), HttpStatus.OK) ;
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an existing offer",
            code = 204)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Offer doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Order> deleteOrder(@PathVariable long id) {
        try {
            orderService.delete(id);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for order items of the order",
            response = OrderItem.class,
            responseContainer = "Set")
    @ApiResponses({
            @ApiResponse(code = 204, message = "There are no requested order items"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/orderItems", method = RequestMethod.GET)
    public ResponseEntity<Set<OrderItem>> findOrderItemsOfOrder(@PathVariable long id) {
        try {
            return new ResponseEntity<>(orderService.findOrderItems(id), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Adding order item to existing order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/orderItem", method = RequestMethod.POST)
    public ResponseEntity<Order> addOrderItemToOrder(@PathVariable long id, @RequestBody OrderItem orderItem) {
        try {
            return new ResponseEntity<>(orderService.addOrderItem(id, orderItem), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Removing order item from existing order",
            response = Order.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/orderItem", method = RequestMethod.DELETE)
    public ResponseEntity<Order> removeOrderItemFromOrder(@PathVariable long id, @RequestBody long orderItemId) {
        try {
            return new ResponseEntity<>(orderService.removeOrderItem(id, orderItemId), HttpStatus.OK);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for orders with requested payment status",
            response = Order.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 204, message = "There are no orders with requested payment status"),
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong payment status value")
    })
    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> findOrdersByPaymentStatus(@PathVariable int status) {
        try {
            return new ResponseEntity<>(orderService.findOrdersByPaymentStatus(status), HttpStatus.OK);
        } catch (PaymentStatusException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Payment for the order",
            response = Order.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 409, message = "The order is already paid"),
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "Order doesn't exist")
    })
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<Order> payForOrder(@PathVariable long id) {
        try {
            return new ResponseEntity<>(orderService.payForOrder(id), HttpStatus.OK);
        } catch (PayForOrderException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (PersistenceMethodException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

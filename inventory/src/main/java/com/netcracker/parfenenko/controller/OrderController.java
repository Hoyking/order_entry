package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.dto.FreshOrderDto;
import com.netcracker.parfenenko.dto.OrderDto;
import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.mapper.FreshOrderDtoMapper;
import com.netcracker.parfenenko.mapper.OrderDtoMapper;
import com.netcracker.parfenenko.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    private OrderDtoMapper orderMapper;
    private FreshOrderDtoMapper freshOrderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderDtoMapper orderMapper, FreshOrderDtoMapper freshOrderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.freshOrderMapper = freshOrderMapper;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Saving a new order",
            response = OrderDto.class,
            code = 201)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<OrderDto> saveOrder(@RequestBody FreshOrderDto orderDto) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.save(freshOrderMapper.mapDto(orderDto))),
                HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by id",
            response = OrderDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "There is no order with such id"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderDto> findOrderById(@PathVariable long id) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.findById(id)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for an order by name",
            response = OrderDto.class)
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "There is no order with such name")
    })
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<OrderDto> findOrderByName(@PathVariable String name) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.findByName(name)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for all orders",
            response = OrderDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<OrderDto>> findAllOrders() {
        return new ResponseEntity<>(orderMapper.mapEntityCollection(orderService.findAll()), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Updating an existing order",
            response = OrderDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<OrderDto> updateOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.update(order)), HttpStatus.OK) ;
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deleting an existing order",
            code = 204)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<OrderDto> deleteOrder(@PathVariable long id) {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for order items of the order",
            response = OrderItem.class,
            responseContainer = "Set")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/orderItems", method = RequestMethod.GET)
    public ResponseEntity<Set<OrderItem>> findOrderItemsOfOrder(@PathVariable long id) {
        return new ResponseEntity<>(orderService.findOrderItems(id), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Adding order item to existing order",
            response = OrderDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/orderItem", method = RequestMethod.POST)
    public ResponseEntity<OrderDto> addOrderItemToOrder(@PathVariable long id, @RequestBody OrderItem orderItem) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.addOrderItem(id, orderItem)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Removing order item from existing order",
            response = OrderDto.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 500, message = "Oops, something went wrong")
    })
    @RequestMapping(value = "/{id}/orderItem", method = RequestMethod.DELETE)
    public ResponseEntity<OrderDto> removeOrderItemFromOrder(@PathVariable long id, @RequestBody long orderItemId) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.removeOrderItem(id, orderItemId)), HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Searching for orders with requested status",
            response = OrderDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 400, message = "Wrong payment status value")
    })
    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDto>> findOrdersByPaymentStatus(@PathVariable int status) {
        return new ResponseEntity<>(orderMapper.mapEntityCollection(orderService.findOrdersByPaymentStatus(status)),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Payment for the order",
            response = OrderDto.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Oops, something went wrong"),
            @ApiResponse(code = 404, message = "Order doesn't exist"),
            @ApiResponse(code = 400, message = "Wrong payment status")
    })
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable long id, @RequestBody int status) {
        return new ResponseEntity<>(orderMapper.mapEntity(orderService.updateStatus(id, status)), HttpStatus.OK);
    }

}

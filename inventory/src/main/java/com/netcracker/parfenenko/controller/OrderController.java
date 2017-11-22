package com.netcracker.parfenenko.controller;

import com.netcracker.parfenenko.entities.InventoryOrder;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveOrder(@RequestBody InventoryOrder order) {
        orderService.save(order);
    }

    @RequestMapping(value = "/find_by_id", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public InventoryOrder findOrderById(@RequestParam(name = "order_id") long id) {
        return orderService.findById(id);
    }

    @RequestMapping(value = "/find_by_name", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public InventoryOrder findOrderByName(@RequestParam(name = "name") String name) {
        return orderService.findByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public List<InventoryOrder> findAllOrders() {
        return orderService.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void updateOrder(@RequestBody InventoryOrder order) {
        orderService.update(order);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(@RequestParam(name = "order_id") long id) {
        orderService.delete(id);
    }

    @RequestMapping(value = "/add_order_item", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void addOrderItemToOrder(@RequestParam(name = "order_id") long orderId,
                                    @RequestParam(name = "order_item") OrderItem orderItem) {
        orderService.addOrderItem(orderId, orderItem);
    }

    @RequestMapping(value = "/remove_order_item", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public void removeOrderItemFromOrder(@RequestParam(name = "order_id") long orderId,
                                    @RequestParam(name = "order_item") OrderItem orderItem) {
        orderService.removeOrderItem(orderId, orderItem);
    }

}

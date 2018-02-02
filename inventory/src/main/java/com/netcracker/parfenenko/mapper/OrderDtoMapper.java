package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.OrderDto;
import com.netcracker.parfenenko.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper extends GenericDtoMapper<Order, OrderDto> {

    public OrderDtoMapper() {
        super(Order.class, OrderDto.class);
    }

}

package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.OrderItemDto;
import com.netcracker.parfenenko.entities.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDtoMapper extends GenericDtoMapper<OrderItem, OrderItemDto> {

    public OrderItemDtoMapper() {
        super(OrderItem.class, OrderItemDto.class);
    }

}

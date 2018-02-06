package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.UpdateOrderDto;
import com.netcracker.parfenenko.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderDtoMapper extends GenericDtoMapper<Order, UpdateOrderDto> {

    public UpdateOrderDtoMapper() {
        super(Order.class, UpdateOrderDto.class);
    }

}

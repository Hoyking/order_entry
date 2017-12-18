package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.FreshOrderDto;
import com.netcracker.parfenenko.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class FreshOrderDtoMapper extends GenericDtoMapper<Order, FreshOrderDto> {

    public FreshOrderDtoMapper() {
        super(Order.class, FreshOrderDto.class);
    }

}

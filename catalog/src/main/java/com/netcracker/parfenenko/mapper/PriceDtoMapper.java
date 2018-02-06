package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.PriceDto;
import com.netcracker.parfenenko.entity.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceDtoMapper extends GenericDtoMapper<Price, PriceDto> {

    public PriceDtoMapper() {
        super(Price.class, PriceDto.class);
    }

}

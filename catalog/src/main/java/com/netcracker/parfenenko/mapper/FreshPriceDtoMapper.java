package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.FreshPriceDto;
import com.netcracker.parfenenko.entity.Price;
import org.springframework.stereotype.Component;

@Component
public class FreshPriceDtoMapper extends GenericDtoMapper<Price, FreshPriceDto> {

    public FreshPriceDtoMapper() {
        super(Price.class, FreshPriceDto.class);
    }

}

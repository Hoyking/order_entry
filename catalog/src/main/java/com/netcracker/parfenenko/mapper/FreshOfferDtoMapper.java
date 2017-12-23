package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.FreshOfferDto;
import com.netcracker.parfenenko.entities.Offer;
import org.springframework.stereotype.Component;

@Component
public class FreshOfferDtoMapper extends GenericDtoMapper<Offer, FreshOfferDto> {

    protected FreshOfferDtoMapper() {
        super(Offer.class, FreshOfferDto.class);
    }

}

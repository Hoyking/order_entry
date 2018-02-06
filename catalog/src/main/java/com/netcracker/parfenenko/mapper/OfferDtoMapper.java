package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.OfferDto;
import com.netcracker.parfenenko.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferDtoMapper extends GenericDtoMapper<Offer, OfferDto> {

    public OfferDtoMapper() {
        super(Offer.class, OfferDto.class);
    }

}

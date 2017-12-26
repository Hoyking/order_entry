package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.UpdateOfferDto;
import com.netcracker.parfenenko.entities.Offer;
import org.springframework.stereotype.Component;

@Component
public class UpdateOfferDtoMapper extends GenericDtoMapper<Offer, UpdateOfferDto> {

    public UpdateOfferDtoMapper() {
        super(Offer.class, UpdateOfferDto.class);
    }

}

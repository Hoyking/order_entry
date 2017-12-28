package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateOfferDto extends NamedDto {

    private FreshPriceDto price;
    private OfferCategoryDto category;
    private String description;
    private boolean available;

}

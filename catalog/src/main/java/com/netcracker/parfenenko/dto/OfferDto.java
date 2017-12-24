package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfferDto extends NamedDto {

    private PriceDto price;
    private CategoryDto category;
    private String description;
    private boolean available;

}

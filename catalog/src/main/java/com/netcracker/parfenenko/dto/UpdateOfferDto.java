package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateOfferDto extends NamedDto {

    private FreshPriceDto price;
    private Set<TagDto> tags;
    private OfferCategoryDto category;
    private String description;
    private boolean available;

}

package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode
public class FreshOfferDto {

    private String name;
    private FreshPriceDto price;
    private OfferCategoryDto category;
    private Set<TagDto> tags;
    private String description;

}

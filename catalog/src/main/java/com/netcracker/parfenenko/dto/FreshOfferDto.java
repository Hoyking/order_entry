package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode
public class FreshOfferDto {

    private PriceDto price;
    private CategoryDto category;
    private Set<TagDto> tags;
    private String description;

}

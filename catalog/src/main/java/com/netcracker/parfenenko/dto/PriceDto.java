package com.netcracker.parfenenko.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceDto extends IdentifiedDto {

    private double value;

}

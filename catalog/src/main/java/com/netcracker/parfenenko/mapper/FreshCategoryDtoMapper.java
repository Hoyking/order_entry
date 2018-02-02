package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.FreshCategoryDto;
import com.netcracker.parfenenko.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class FreshCategoryDtoMapper extends GenericDtoMapper<Category, FreshCategoryDto> {

    public FreshCategoryDtoMapper() {
        super(Category.class, FreshCategoryDto.class);
    }

}

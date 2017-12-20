package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.CategoryDto;
import com.netcracker.parfenenko.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoMapper extends GenericDtoMapper<Category, CategoryDto> {

    public CategoryDtoMapper() {
        super(Category.class, CategoryDto.class);
    }

}

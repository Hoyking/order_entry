package com.netcracker.parfenenko.mapper;

import com.netcracker.parfenenko.dto.TagDto;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapper extends GenericDtoMapper<Tag, TagDto> {

    public TagDtoMapper() {
        super(Tag.class, TagDto.class);
    }
}

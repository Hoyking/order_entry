package com.netcracker.parfenenko.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericDtoMapper<E, D> implements DtoMapper<E, D> {

    private ModelMapper modelMapper;
    private Class<E> entityClass;
    private Class<D> dtoClass;

    protected GenericDtoMapper(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Autowired
    private void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public D mapEntity(E entity) {
        return modelMapper.map(entity, dtoClass);
    }

    @Override
    public E mapDto(D dto) {
        return modelMapper.map(dto, entityClass);
    }

    @Override
    public List<D> mapEntityCollection(Collection<E> entities) {
        return entities.stream()
                .map(this::mapEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<E> mapDtoCollection(@SuppressWarnings("SpellCheckingInspection") Collection<D> dtos) {
        return dtos.stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

}

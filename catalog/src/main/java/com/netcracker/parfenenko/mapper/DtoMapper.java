package com.netcracker.parfenenko.mapper;

import java.util.Collection;
import java.util.List;

public interface DtoMapper<E, D> {

    D mapEntity(E entity);

    E mapDto(D dto);

    List<D> mapEntityCollection(Collection<E> entities);

    @SuppressWarnings("SpellCheckingInspection")
    List<E> mapDtoCollection(Collection<D> dtos);

}

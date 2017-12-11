package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface GenericDAO<T, ID> {

    T save(T entity) throws PersistenceMethodException;

    T findById(ID id) throws PersistenceMethodException, EntityNotFoundException;

    List<T> findAll() throws PersistenceMethodException, EntityNotFoundException;

    T update(T entity) throws PersistenceMethodException, EntityNotFoundException;

    void delete(ID id) throws PersistenceMethodException, EntityNotFoundException;

}

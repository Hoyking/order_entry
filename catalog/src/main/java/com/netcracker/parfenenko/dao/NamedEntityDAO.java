package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface NamedEntityDAO<T, ID> extends GenericDAO<T, ID> {

    T findByName(String name) throws PersistenceMethodException, EntityNotFoundException;

    List<T> findByPartOfName(String part) throws PersistenceMethodException;
    
}

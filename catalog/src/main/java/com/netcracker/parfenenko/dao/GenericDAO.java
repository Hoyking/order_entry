package com.netcracker.parfenenko.dao;

import java.util.List;

public interface GenericDAO<T, ID> {

    ID save(T entity);

    T findById(ID id);

    List<T> findAll();

    void update(T entity);

    void delete(ID id);

}

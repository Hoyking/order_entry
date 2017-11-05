package com.netcracker.parfenenko.dao;

public interface NamedEntityDAO<T, ID> extends GenericDAO<T, ID> {

    T findByName(String name);

}

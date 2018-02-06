package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface TagDAO extends NamedEntityDAO<Tag, Long> {

    List<Offer> findTagOffers(String name) throws PersistenceMethodException, EntityNotFoundException;

}

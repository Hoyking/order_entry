package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;

import java.util.List;

public interface TagDAO extends NamedEntityDAO<Tag, Long> {

    List<Offer> findTagOffers(long id);

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CategoryDAO extends NamedEntityDAO<Category, Long> {

    List<Offer> findCategoryOffers(long id) throws PersistenceMethodException, EntityNotFoundException;

    Category addOffer(long categoryId, long offerId) throws PersistenceMethodException, EntityNotFoundException;

}

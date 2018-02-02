package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

public interface OfferDAO extends NamedEntityDAO<Offer, Long> {

    List<Offer> findByFilters(List<Long> categories, List<String> tags, Double from, Double to)
            throws PersistenceMethodException, EntityNotFoundException;

    Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException;

    List<Offer> findByTags(List<String> tags) throws PersistenceMethodException, EntityNotFoundException;

    List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException;

    List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException;

}

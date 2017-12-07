package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

public interface OfferDAO extends NamedEntityDAO<Offer, Long> {

    Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException;

    Offer changeAvailability(long id) throws PersistenceMethodException, EntityNotFoundException;

    List<Offer> findOffersByTags(List<Tag> tags) throws PersistenceMethodException, EntityNotFoundException;

    List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException;

    Offer addPriceToOffer(long id, Price price) throws PersistenceMethodException, EntityNotFoundException;

    List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException;

    Offer addTagToOffer(long id, Tag tag) throws PersistenceMethodException, EntityNotFoundException;

    Offer removeTagFromOffer(long id, Tag tag) throws PersistenceMethodException, EntityNotFoundException;

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;

import java.util.List;
import java.util.Set;

public interface OfferDAO extends NamedEntityDAO<Offer, Long> {

    Set<Tag> findTags(long offerId);

    Offer changeAvailability(long id);

    List<Offer> findOffersByTags(List<Tag> tags);

    List<Offer> findAvailableOffers();

    Offer addPriceToOffer(long id, Price price);

    List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice);

    Offer addTagToOffer(long id, Tag tag);

    Offer removeTagFromOffer(long id, Tag tag);

}

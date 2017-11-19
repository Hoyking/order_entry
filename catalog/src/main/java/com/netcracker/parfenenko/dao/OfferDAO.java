package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;

import java.util.List;

public interface OfferDAO extends NamedEntityDAO<Offer, Long> {

    Offer changeAvailability(long id);

    List<Offer> findOffersByTags(List<Tag> tags);

    List<Offer> findAvailableOffers();

    Offer addPriceToOffer(long id, Price price);

    List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice);

    Offer addTagToOffer(long id, Tag tag);

    Offer removeTagFromOffer(long id, Tag tag);

    Offer addOfferToCategory(long offerId, long categoryId);

    Offer removeOfferFromCategory(long offerId);

}

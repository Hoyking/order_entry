package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JPAOfferDAO extends JPANamedEntityDAO<Offer, Long> implements OfferDAO {

    private final String AVAILABLE_OFFERS = "SELECT e FROM " + Offer.class.getName() + " e WHERE e.available = true";
    private final String OFFERS_OF_PRICE_INTERVAL = "SELECT e FROM " + Offer.class.getName() +
            " e WHERE e.price.value >= ?1 AND e.price.value <= ?2";
    private final String ADD_OFFER_TO_CATEGORY = "UPDATE " + Offer.class.getName() + " offer SET offer.category = " +
            Category.class.getName() + " category WHERE offer.id = ?1 AND category.id = ?2";
    private final String REMOVE_OFFER_FROM_CATEGORY = "UPDATE " + Offer.class.getName() + " offer SET offer.category = NULL " +
            "WHERE offer.id = ?1";

    public JPAOfferDAO() {
        super.setPersistenceClass(Offer.class);
    }

    @Override
    public Offer changeAvailability(long id) {
        Offer offer = findById(id);
        offer.setAvailable(!offer.isAvailable());
        return update(offer);
    }

    @Override
    public List<Offer> findOffersByTags(List<Tag> tags) {
        return findAll().stream().filter(offer -> offer.getTags().containsAll(tags)).collect(Collectors.toList());
    }

    @Override
    public List<Offer> findAvailableOffers() {
        return (List<Offer>) transactions.startGenericTransaction(entityManager ->
                (List<Offer>) entityManager
                        .createQuery(AVAILABLE_OFFERS)
                        .getResultList());
    }

    @Override
    public Offer addPriceToOffer(long id, Price price) {
        Offer offer = findById(id);
        offer.setPrice(price);
        return update(offer);
    }

    @Override
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) {
        return (List<Offer>) transactions.startGenericTransaction(entityManager ->
                (List<Offer>) entityManager
                        .createQuery(OFFERS_OF_PRICE_INTERVAL)
                        .setParameter(1, fromPrice)
                        .setParameter(2, toPrice)
                        .getResultList());
    }

    @Override
    public Offer addTagToOffer(long id, Tag tag) {
        Offer offer = findById(id);
        offer.getTags().add(tag);
        return update(offer);
    }

    @Override
    public Offer removeTagFromOffer(long id, Tag tag) {
        Offer offer = findById(id);
        offer.getTags().remove(tag);
        return update(offer);
    }

    @Override
    public Offer addOfferToCategory(long offerId, long categoryId) {
        transactions.startTransaction(entityManager ->
                entityManager
                        .createQuery(ADD_OFFER_TO_CATEGORY)
                        .setParameter(1, offerId)
                        .setParameter(2, categoryId));
        return findById(offerId);
    }

    @Override
    public Offer removeOfferFromCategory(long offerId) {
        transactions.startTransaction(entityManager ->
                entityManager
                        .createQuery(REMOVE_OFFER_FROM_CATEGORY)
                        .setParameter(1, offerId));
        return findById(offerId);
    }

}

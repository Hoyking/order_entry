package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JPAOfferDAO extends JPANamedEntityDAO<Offer, Long> implements OfferDAO {

    private final String ADD_OFFER_TO_CATEGORY = "UPDATE Offer SET category_id = ?1 WHERE id = ?2";
    private final String REMOVE_OFFER_FROM_CATEGORY = "UPDATE " + Offer.class.getName() + " offer SET offer.category = NULL " +
            "WHERE offer.id = ?1";

    public JPAOfferDAO() {
        super.setPersistenceClass(Offer.class);
    }

    @Override
    public Offer save(Offer entity) {
        if(entity.getTags() != null) {
            for(Tag tag: entity.getTags()) {
                tag.setId(0);
            }
        }
        return super.save(entity);
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
        return transactions.startGenericTransaction(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
            Root<Offer> root = criteriaQuery.from(Offer.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("available"), true));

            TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        });
    }

    @Override
    public Offer addPriceToOffer(long id, Price price) {
        Offer offer = findById(id);
        offer.setPrice(price);
        return update(offer);
    }

    @Override
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) {
        return transactions.startGenericTransaction(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
            Root<Offer> root = criteriaQuery.from(Offer.class);
            ParameterExpression<Double> param1 = criteriaBuilder.parameter(Double.class);
            ParameterExpression<Double> param2 = criteriaBuilder.parameter(Double.class);
            Expression<Double> expression = root.get("price").get("value");
            criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.gt(expression, param1),
                    criteriaBuilder.le(expression, param2)));

            TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
            query.setParameter(param1, fromPrice);
            query.setParameter(param2, toPrice);
            return query.getResultList();
        });
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
                        .createNativeQuery(ADD_OFFER_TO_CATEGORY)
                        .setParameter(2, offerId)
                        .setParameter(1, categoryId)
                        .executeUpdate());
        return findById(offerId);
    }

    @Override
    public Offer removeOfferFromCategory(long offerId) {
        transactions.startTransaction(entityManager ->
            entityManager
                    .createQuery(REMOVE_OFFER_FROM_CATEGORY)
                    .setParameter(1, offerId)
                    .executeUpdate());
        return findById(offerId);
    }

}

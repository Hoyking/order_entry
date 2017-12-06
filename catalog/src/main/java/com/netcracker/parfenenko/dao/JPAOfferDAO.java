package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JPAOfferDAO extends JPANamedEntityDAO<Offer, Long> implements OfferDAO {

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
    public Set<Tag> findTags(long offerId) {
        return persistenceMethodsProvider.functionalMethod(entityManager ->
                new HashSet<>(entityManager
                        .createNamedQuery("findTags")
                        .setParameter(1, offerId)
                        .getResultList()));
    }

    @Override
    public Offer changeAvailability(long id) {
        Offer offer = findById(id);
        offer.setAvailable(!offer.isAvailable());
        return update(offer);
    }

    @Override
    public List<Offer> findOffersByTags(List<Tag> tags) {
        return findAll().stream().filter(offer -> findTags(offer.getId()).containsAll(tags))
                .collect(Collectors.toList());
    }

    @Override
    public List<Offer> findAvailableOffers() {
        return persistenceMethodsProvider.functionalMethod(this::availableOffers);
    }

    @Override
    public Offer addPriceToOffer(long id, Price price) {
        Offer offer = findById(id);
        offer.setPrice(price);
        return update(offer);
    }

    @Override
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) {
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> offersOfPriceInterval(entityManager, fromPrice, toPrice));
    }

    @Override
    public Offer addTagToOffer(long id, Tag tag) {
        Offer offer = findById(id);
        tag.setId(0);
        offer.getTags().add(tag);
        return update(offer);
    }

    @Override
    public Offer removeTagFromOffer(long id, Tag tag) {
        Offer offer = findById(id);
        offer.getTags().remove(tag);
        return update(offer);
    }

    private List<Offer> availableOffers(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root<Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("available"), true));

        TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private List<Offer> offersOfPriceInterval(EntityManager entityManager, double fromPrice, double toPrice) {
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
    }

}

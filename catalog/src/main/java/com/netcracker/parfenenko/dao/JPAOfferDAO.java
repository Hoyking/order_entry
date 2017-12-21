package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class JPAOfferDAO extends JPANamedEntityDAO<Offer, Long> implements OfferDAO {

    private static final Logger logger = LogManager.getLogger(JPAOfferDAO.class);

    public JPAOfferDAO() {
        super.setPersistenceClass(Offer.class);
    }

    @Override
    public Offer save(Offer entity) throws PersistenceMethodException, EntityNotFoundException {
        if(entity.getTags() != null) {
            for(Tag tag: entity.getTags()) {
                tag.setId(0);
            }
        }
        return super.save(entity);
    }

    @Override
    public List<Offer> findByFilters(List<Long> categories, List<String> tags, double from, double to)
            throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for offers with filters";
        return persistenceMethodsProvider.
                functionalMethod(entityManager -> findByFiltersQuery(entityManager, categories, tags, from, to),
                        operation);
    }

    @Override
    public Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for tags of offer with id " + offerId;
        return persistenceMethodsProvider.functionalMethod(entityManager -> findTagsQuery(entityManager, offerId),
                operation);
    }

    @Override
    public Offer changeAvailability(long id) throws PersistenceMethodException, EntityNotFoundException {
        Offer offer = findById(id);
        offer.setAvailable(!offer.isAvailable());
        return update(offer);
    }

    @Override
    public List<Offer> findOffersByTags(List<String> tags) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for offers by tags";
        return persistenceMethodsProvider.functionalMethod(entityManager ->
                entityManager
                        .createNamedQuery("findByTags", Offer.class)
                        .setParameter("tags", tags)
                        .getResultList()
                , operation
        );
    }

    @Override
    public List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for available offers";
        return persistenceMethodsProvider.functionalMethod(this::availableOffers,
                operation);
    }

    @Override
    public Offer addPriceToOffer(long id, Price price) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "adding price to the offer with id " + id;
        logger.info("START OPERATION: " + operation);
        try {
            Offer offer = findById(id);
            offer.setPrice(price);
            offer = update(offer);
            logger.info("END OF OPERATION: " + operation);
            return offer;
        } catch (Exception e) {
            logger.error("There is an error occurred while executing operation of " +
                    operation + ". Stack trace:", e);
            throw e;
        }
    }

    @Override
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException {
        String operation = "searching for offers of price interval (" + fromPrice + ", " + toPrice + ")";
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> offersOfPriceInterval(entityManager, fromPrice, toPrice),
                        operation);
    }

    @Override
    public Offer addTagToOffer(long id, Tag tag) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "adding tag to the offer with id " + id;
        logger.info("START OPERATION: " + operation);
        try {
            Offer offer = findById(id);
            tag.setId(0);
            offer.getTags().add(tag);
            offer = update(offer);
            logger.info("END OF OPERATION: " + operation);
            return offer;
        } catch (Exception e) {
            logger.error("There is an error occurred while executing operation of " +
                    operation + ". Stack trace:", e);
            throw e;
        }
    }

    @Override
    public Offer removeTagFromOffer(long id, Tag tag) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "removing tag from the offer with id " + id;
        logger.info("START OPERATION: " + operation);
        try {
            Offer offer = findById(id);
            tag.setId(0);
            offer.getTags().remove(tag);
            offer = update(offer);
            logger.info("END OF OPERATION: " + operation);
            return offer;
        } catch (Exception e) {
            logger.error("There is an error occurred while executing operation of " +
                    operation + ". Stack trace:", e);
            throw e;
        }
    }

    private List<Offer> findByFiltersQuery(EntityManager entityManager, List<Long> categories, List<String> tags,
                                           double from, double to) {
        if (categories == null && tags == null) {
            return entityManager
                    .createNamedQuery("findByPrice", Offer.class)
                    .setParameter("fromPrice", from)
                    .setParameter("toPrice", to)
                    .getResultList();
        } else if (categories == null) {
            return entityManager
                    .createNamedQuery("findByTagsAndPrice", Offer.class)
                    .setParameter("tags", tags)
                    .setParameter("fromPrice", from)
                    .setParameter("toPrice", to)
                    .getResultList();
        } else if (tags == null) {
            return entityManager
                    .createNamedQuery("findByCategoriesAndPrice", Offer.class)
                    .setParameter("categories", categories)
                    .setParameter("fromPrice", from)
                    .setParameter("toPrice", to)
                    .getResultList();
        }
        return entityManager
                .createNamedQuery("findByAllFilters", Offer.class)
                .setParameter("categories", categories)
                .setParameter("tags", tags)
                .setParameter("fromPrice", from)
                .setParameter("toPrice", to)
                .getResultList();
    }

    private Set<Tag> findTagsQuery(EntityManager entityManager, long offerId) {
//        noinspection unchecked
        return new HashSet(entityManager
                .createNamedQuery("findTags", Set.class)
                .setParameter(1, offerId)
                .getResultList());
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

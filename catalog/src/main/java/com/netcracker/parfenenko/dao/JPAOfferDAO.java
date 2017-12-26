package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
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

    public JPAOfferDAO() {
        super.setPersistenceClass(Offer.class);
    }

    @Override
    public Offer save(Offer entity) throws PersistenceMethodException, EntityNotFoundException {
        if (entity.getTags() != null) {
            for (Tag tag : entity.getTags()) {
                tag.setId(0);
            }
        }
        entity.setAvailable(true);
        return super.save(entity);
    }

    @Override
    public List<Offer> findByFilters(List<Long> categories, List<String> tags, double from, double to)
            throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> findByFiltersQuery(entityManager, categories, tags, from, to));
    }

    @Override
    public List<Offer> findByCategoriesAndTags(List<Long> categories, List<String> tags) throws PersistenceMethodException,
            EntityNotFoundException {
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> findByCategoriesAndTagsQuery(entityManager, categories, tags));
    }

    @Override
    public Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(entityManager -> findTagsQuery(entityManager, offerId));
    }

    @Override
    public List<Offer> findByTags(List<String> tags) throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(entityManager ->
                entityManager
                        .createNamedQuery("findByTags", Offer.class)
                        .setParameter("tags", tags)
                        .getResultList()
        );
    }

    @Override
    public List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(this::availableOffersQuery);
    }

    @Override
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException {
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> offersOfPriceIntervalQuery(entityManager, fromPrice, toPrice));
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

    private List<Offer> findByCategoriesAndTagsQuery(EntityManager entityManager, List<Long> categories,
                                                     List<String> tags) {
        if (categories == null && tags == null) {
            return findAll();
        } else if (categories == null) {
            return entityManager
                    .createNamedQuery("findByTags", Offer.class)
                    .setParameter("tags", tags)
                    .getResultList();
        } else if (tags == null) {
            return entityManager
                    .createNamedQuery("findByCategories", Offer.class)
                    .setParameter("categories", categories)
                    .getResultList();
        }
        return entityManager
                .createNamedQuery("findByCategoriesAndTags", Offer.class)
                .setParameter("categories", categories)
                .setParameter("tags", tags)
                .getResultList();
    }

    private Set<Tag> findTagsQuery(EntityManager entityManager, long offerId) {
//        noinspection unchecked
        return new HashSet(entityManager
                .createNamedQuery("findTags", Set.class)
                .setParameter(1, offerId)
                .getResultList());
    }

    private List<Offer> availableOffersQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root<Offer> root = criteriaQuery.from(Offer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("available"), true));

        TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private List<Offer> offersOfPriceIntervalQuery(EntityManager entityManager, double fromPrice, double toPrice) {
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

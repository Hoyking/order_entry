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
    public List<Offer> findByFilters(List<Long> categories, List<String> tags, Double from, Double to)
            throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(entityManager ->
                entityManager
                        .createQuery(findByFiltersQuery(categories, tags, from, to), Offer.class)
                        .getResultList());
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

    private String findByFiltersQuery(List<Long> categories, List<String> tags, Double from, Double to) {
        StringBuilder query = new StringBuilder("SELECT DISTINCT o FROM Offer o");
        final String BY_TAGS = " JOIN Tag t ON (t.name IN %s AND t MEMBER OF o.tags)";
        final String BY_CATEGORIES = " o.category.id IN %s";
        final String BY_FROM = " o.price.value >= %s";
        final String BY_TO = " o.price.value <= %s";
        final String AVAILABLE = " o.available = true";
        final String WHERE = " WHERE";
        final String AND = " AND";

        if (tags != null) {
            query.append(String.format(BY_TAGS, getStringParamStr(tags)));
        }
        query.append(WHERE);
        if (categories != null) {
            query.append(String.format(BY_CATEGORIES, getLongParamStr(categories))).append(AND);
        }
        if (from != null) {
            query.append(String.format(BY_FROM, from)).append(AND);
        }
        if (to != null) {
            query.append(String.format(BY_TO, to)).append(AND);
        }
        query.append(AVAILABLE);
        return query.toString();
    }

    private String getStringParamStr(List<String> list) {
        StringBuilder str = new StringBuilder("(");
        list.forEach(item -> str
                .append("'")
                .append(item)
                .append("'")
                .append(", "));
        if (list.size() != 0) {
            str.setLength(str.length() - 2);
        }
        str.append(")");
        return str.toString();
    }

    private String getLongParamStr(List<Long> list) {
        StringBuilder str = new StringBuilder("(");
        list.forEach(item -> str
                .append(item)
                .append(", "));
        if (list.size() != 0) {
            str.setLength(str.length() - 2);
        }
        str.append(")");
        return str.toString();
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

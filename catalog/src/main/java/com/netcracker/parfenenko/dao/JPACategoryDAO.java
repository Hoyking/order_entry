package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class JPACategoryDAO extends JPANamedEntityDAO<Category, Long> implements CategoryDAO {

    private final String ADD_OFFER_TO_CATEGORY = "UPDATE Offer SET category_id = ?1 WHERE id = ?2";
    private final String REMOVE_OFFER_FROM_CATEGORY = "UPDATE " + Offer.class.getName() + " offer SET offer.category = NULL " +
            "WHERE offer.category.id = ?1 AND offer.id = ?2";

    public JPACategoryDAO() {
        super.setPersistenceClass(Category.class);
    }

    @Override
    public List<Offer> findCategoryOffers(long id) {
        return persistenceMethodsProvider.functionalMethod(entityManager -> categoryOffers(entityManager, id));
    }

    @Override
    public Category addOffer(long categoryId, long offerId) {
        persistenceMethodsProvider.consumerMethod(entityManager ->
                entityManager
                        .createNativeQuery(ADD_OFFER_TO_CATEGORY)
                        .setParameter(1, categoryId)
                        .setParameter(2, offerId)
                        .executeUpdate());
        return findById(categoryId);
    }

    @Override
    public Category removeOffer(long categoryId, long offerId) {
        persistenceMethodsProvider.consumerMethod(entityManager ->
                entityManager
                        .createQuery(REMOVE_OFFER_FROM_CATEGORY)
                        .setParameter(1, categoryId)
                        .setParameter(2, offerId)
                        .executeUpdate());
        return findById(categoryId);
    }

    private List<Offer> categoryOffers(EntityManager entityManager, long categoryId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root<Offer> root = criteriaQuery.from(Offer.class);
        ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("category").get("id"), parameter));

        TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
        query.setParameter(parameter, categoryId);
        return query.getResultList();
    }

}

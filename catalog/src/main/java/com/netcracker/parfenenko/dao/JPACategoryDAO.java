package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.util.Transactions;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class JPACategoryDAO extends JPANamedEntityDAO<Category, Long> implements CategoryDAO {

    public JPACategoryDAO() {
        super.setPersistenceClass(Category.class);
    }

    @Override
    public List<Offer> findCategoryOffers(long id) {
        return transactions.startGenericTransaction(entityManager -> {
                    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
                    Root<Offer> root = criteriaQuery.from(Offer.class);
                    ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
                    criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("category").get("id"), parameter));

                    TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
                    query.setParameter(parameter, id);
                    return query.getResultList();
                });
    }

}

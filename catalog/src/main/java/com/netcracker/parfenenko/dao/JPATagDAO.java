package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

@Repository
public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    public JPATagDAO() {
        super.setPersistenceClass(Tag.class);
    }

    @Override
    public List<Offer> findTagOffers(long id) {
        return transactions.startGenericTransaction(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
            Root<Offer> root = criteriaQuery.from(Offer.class);
            ParameterExpression<Tag> parameter = criteriaBuilder.parameter(Tag.class);
            Expression<Collection<Tag>> tags = root.get("tags");
            criteriaQuery.select(root).where(criteriaBuilder.isMember(parameter, tags));

            TypedQuery<Offer> query = entityManager.createQuery(criteriaQuery);
            query.setParameter(parameter, findById(id));
            return query.getResultList();
        });
    }

}

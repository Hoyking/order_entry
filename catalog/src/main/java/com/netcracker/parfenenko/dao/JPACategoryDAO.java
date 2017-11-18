package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.util.Transactions;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class JPACategoryDAO extends JPANamedEntityDAO<Category, Long> implements CategoryDAO {

    private final String CATEGORY_OFFERS = "SELECT e FROM " + Offer.class.getName() + " e WHERE e.category.id = ?1";

    public JPACategoryDAO() {
        super.setPersistenceClass(Category.class);
    }

    @Override
    public List<Offer> findCategoryOffers(Category category) {
        return (List<Offer>) transactions.startGenericTransaction(entityManager ->
                (List<Offer>) (entityManager
                        .createQuery(CATEGORY_OFFERS)
                        .setParameter(1, category.getId())
                        .getResultList()));
    }

}

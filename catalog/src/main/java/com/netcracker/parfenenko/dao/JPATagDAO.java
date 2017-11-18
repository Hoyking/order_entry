package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    private final String TAG_OFFERS = "SELECT e FROM " + Offer.class.getName() + " e WHERE ?1 MEMBER OF e.tags";

    public JPATagDAO() {
        super.setPersistenceClass(Tag.class);
    }

    @Override
    public List<Offer> findTagOffers(long id) {
        Tag tag = findById(id);
        return (List<Offer>) transactions.startGenericTransaction(entityManager ->
                (List<Offer>) (entityManager
                        .createQuery(TAG_OFFERS)
                        .setParameter(1, tag)
                        .getResultList()));
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    public JPATagDAO() {
        super.setPersistenceClass(Tag.class);
    }

    @Override
    public Tag save(Tag entity) {
        entity.setId(0);
        return super.save(entity);
    }

    @Override
    public List<Offer> findTagOffers(String name) {
        return (List<Offer>) persistenceMethodsProvider.functionalMethod(entityManager ->
            (List<Offer>) entityManager
                    .createNamedQuery("findOffersByTag")
                    .setParameter(1, name)
                    .getResultList()
        );
    }

}

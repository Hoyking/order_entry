package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    public JPATagDAO() {
        super.setPersistenceClass(Tag.class);
    }

    @Override
    public Tag save(Tag entity) throws PersistenceMethodException {
        entity.setId(0);
        return super.save(entity);
    }

    @Override
    public List<Offer> findTagOffers(String name) throws PersistenceMethodException, EntityNotFoundException {
        String operation = "searching for offers containing tag " + name;
        return (List<Offer>) persistenceMethodsProvider.functionalMethod(entityManager ->
            (List<Offer>) entityManager
                    .createNamedQuery("findOffersByTag")
                    .setParameter(1, name)
                    .getResultList(),
                operation
        );
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Tag;
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
    public Tag findByName(String name) {
        List<Tag> tags = persistenceMethodsProvider.functionalMethod(entityManager ->
                entityManager
                        .createQuery("SELECT e FROM " + getPersistenceClass().getName() +
                                " e WHERE e.name = :name", Tag.class)
                        .setParameter("name", name)
                        .getResultList()
        );
        if (tags.size() == 0) {
            throw new EntityNotFoundException("Tag doesn't exist");
        } else {
            return tags.get(0);
        }
    }

    @Override
    public List<Offer> findTagOffers(String name) throws PersistenceMethodException, EntityNotFoundException {
        return (List<Offer>) persistenceMethodsProvider.functionalMethod(entityManager ->
                (List<Offer>) entityManager
                        .createNamedQuery("findOffersByTag")
                        .setParameter(1, name)
                        .getResultList()
        );
    }

}

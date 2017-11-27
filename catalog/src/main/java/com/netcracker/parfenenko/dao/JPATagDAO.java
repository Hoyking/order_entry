package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    private final String query = "SELECT e FROM Offer e JOIN Tag c ON c.name = ?1 AND c MEMBER OF e.tags";

    public JPATagDAO() {
        super.setPersistenceClass(Tag.class);
    }

    @Override
    public Tag save(Tag entity) {
        entity.setId(0);
        return super.save(entity);
    }

    @Override
    public List<Offer> findTagOffers(long id) {
        return (List<Offer>) transactions.startGenericTransaction(entityManager ->
            (List<Offer>) entityManager
                    .createQuery(query)
                    .setParameter(1, findById(id).getName())
                    .getResultList()
        );
    }

}

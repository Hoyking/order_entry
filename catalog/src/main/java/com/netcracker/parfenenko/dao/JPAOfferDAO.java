package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;
import org.springframework.stereotype.Repository;

@Repository
public class JPAOfferDAO extends JPANamedEntityDAO<Offer, Long> implements OfferDAO {

    public JPAOfferDAO() {
        super.setPersistenceClass(Offer.class);
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Offer;

public class JPAOfferDAO extends JPANamedEntityDAO<Offer, Long> implements OfferDAO {

    private JPAOfferDAO() {
        super(Offer.class);
    }

    private static class Holder {
        static final JPAOfferDAO INSTANCE = new JPAOfferDAO();
    }

    public static JPAOfferDAO getInstance() {
        return Holder.INSTANCE;
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Price;

public class JPAPriceDAO extends JPAGenericDAO<Price, Long> implements PriceDAO {

    private JPAPriceDAO() {
        super(Price.class);
    }

    private static class Holder {
        static final JPAPriceDAO INSTANCE = new JPAPriceDAO();
    }

    public static JPAPriceDAO getInstance() {
        return Holder.INSTANCE;
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;

public class JPAOrderDAO extends JPANamedEntityDAO<Order, Long> implements OrderDAO {

    private JPAOrderDAO() {
        super(Order.class);
    }

    private static class Holder {
        static final JPAOrderDAO INSTANCE = new JPAOrderDAO();
    }

    public static JPAOrderDAO getInstance() {
        return Holder.INSTANCE;
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.InventoryOrder;

public class JPAOrderDAO extends JPANamedEntityDAO<InventoryOrder, Long> implements OrderDAO {

    private JPAOrderDAO() {
        super(InventoryOrder.class);
    }

    private static class Holder {
        static final JPAOrderDAO INSTANCE = new JPAOrderDAO();
    }

    public static JPAOrderDAO getInstance() {
        return Holder.INSTANCE;
    }

}

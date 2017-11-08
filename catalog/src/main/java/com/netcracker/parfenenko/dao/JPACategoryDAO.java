package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;

public class JPACategoryDAO extends JPANamedEntityDAO<Category, Long> implements CategoryDAO {

    private JPACategoryDAO() {
        super(Category.class);
    }

    private static class Holder {
        static final JPACategoryDAO INSTANCE = new JPACategoryDAO();
    }

    public static JPACategoryDAO getInstance() {
        return Holder.INSTANCE;
    }

}

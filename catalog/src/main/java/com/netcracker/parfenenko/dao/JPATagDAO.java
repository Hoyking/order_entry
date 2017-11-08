package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Tag;

public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    public JPATagDAO() {
        super(Tag.class);
    }

    private static class Holder {
        static final JPATagDAO INSTANCE = new JPATagDAO();
    }

    public static JPATagDAO getInstance() {
        return Holder.INSTANCE;
    }

}

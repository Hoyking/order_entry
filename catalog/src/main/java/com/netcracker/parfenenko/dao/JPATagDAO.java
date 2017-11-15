package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Tag;
import org.springframework.stereotype.Repository;

@Repository
public class JPATagDAO extends JPANamedEntityDAO<Tag, Long> implements TagDAO {

    public JPATagDAO() {
        super.setPersistenceClass(Tag.class);
    }

}

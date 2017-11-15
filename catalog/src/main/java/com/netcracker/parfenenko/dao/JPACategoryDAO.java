package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import org.springframework.stereotype.Repository;

@Repository
public class JPACategoryDAO extends JPANamedEntityDAO<Category, Long> implements CategoryDAO {

    public JPACategoryDAO() {
        super.setPersistenceClass(Category.class);
    }

}

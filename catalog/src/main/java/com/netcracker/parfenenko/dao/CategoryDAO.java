package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;

import java.util.List;

public interface CategoryDAO extends NamedEntityDAO<Category, Long> {

    List<Offer> findCategoryOffers(Category category);

}

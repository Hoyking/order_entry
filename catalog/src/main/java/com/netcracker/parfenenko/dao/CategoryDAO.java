package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;

import java.util.List;

public interface CategoryDAO extends NamedEntityDAO<Category, Long> {

    List<Offer> findCategoryOffers(long id);

    Category addOffer(long categoryId, long offerId);

    Category removeOffer(long categoryId, long offerId);

}

package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService {

    private CategoryDAO categoryDAO;

    @Autowired
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category save(Category category) throws PersistenceMethodException {
        return categoryDAO.save(category);
    }

    @Transactional(readOnly = true)
    public Category findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Category findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Category> findByPartOfName(String part) throws PersistenceMethodException {
        return categoryDAO.findByPartOfName(part);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.findAll();
    }

    public Category update(Category category) throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.update(category);
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        categoryDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findCategoryOffers(long id) throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.findCategoryOffers(id);
    }

    public Category addOffer(long categoryId, long offerId) throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.addOffer(categoryId, offerId);
    }

    public Category removeOffer(long categoryId, long offerId) throws PersistenceMethodException, EntityNotFoundException {
        return categoryDAO.removeOffer(categoryId, offerId);
    }

}

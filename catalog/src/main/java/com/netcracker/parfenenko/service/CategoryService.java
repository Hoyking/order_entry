package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private CategoryDAO categoryDAO;

    @Autowired
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Transactional
    public Category save(Category category) {
        return categoryDAO.save(category);
    }

    @Transactional
    public Category findById(long id) {
        return categoryDAO.findById(id);
    }

    @Transactional
    public Category findByName(String name) {
        return categoryDAO.findByName(name);
    }

    @Transactional
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Transactional
    public Category update(Category category) {
        return categoryDAO.update(category);
    }

    @Transactional
    public void delete(long id) {
        categoryDAO.delete(id);
    }

    @Transactional
    public List<Offer> findCategoryOffers(long id) {
        return categoryDAO.findCategoryOffers(id);
    }

}

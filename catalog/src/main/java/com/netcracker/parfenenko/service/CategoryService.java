package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.entity.Category;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.exception.EntityCreationException;
import com.netcracker.parfenenko.exception.EntityDeletingException;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService {

    private static final Logger LOGGER = LogManager.getLogger(CategoryService.class);
    
    private CategoryDAO categoryDAO;

    private String started = "Operation of {} started";
    private String finished = "Operation of {} finished";

    private String save = "saving category";
    private String findById = "searching for category with id %s";
    private String findByName = "searching for category with name %s";
    private String findByPartOfName = "searching for categories which name consist %s";
    private String findAll = "searching for all categories";
    private String update = "updating a category";
    private String delete = "deleting a category with id %s";
    private String findCategoryOffers = "searching for offers of the category with id %s";
    private String addOfferToCategory = "adding an offer to the category with id %s";

    @Autowired
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @SuppressWarnings("Duplicates")
    public Category save(Category category) throws PersistenceMethodException {
        LOGGER.info(started, save);
        try {
            categoryDAO.findByName(category.getName());
        } catch (EntityNotFoundException e) {
            category = categoryDAO.save(category);
            LOGGER.info(finished, save);
            return category;
        }
        throw new EntityCreationException("Category with such name already exists");
    }

    @Transactional(readOnly = true)
    public Category findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findById, id));
        Category category;
        try {
            category = categoryDAO.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.info(finished, String.format(findById, id));
            throw new NoContentException();
        }
        LOGGER.info(finished, String.format(findById, id));
        return category;
    }

    @Transactional(readOnly = true)
    public Category findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findByName, name));
        Category category;
        try {
            category = categoryDAO.findByName(name);
        } catch (EntityNotFoundException e) {
            LOGGER.info(finished, String.format(findByName, name));
            throw new NoContentException();
        }
        LOGGER.info(finished, String.format(findByName, name));
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> findByPartOfName(String part) throws PersistenceMethodException {
        LOGGER.info(started, String.format(findByPartOfName, part));
        List<Category> categories = categoryDAO.findByPartOfName(part);
        LOGGER.info(finished, String.format(findByPartOfName, part));
        return categories;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findAll);
        List<Category> categories = categoryDAO.findAll();
        LOGGER.info(finished, findAll);
        return categories;
    }

    public Category update(Category category) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, update);
        category = categoryDAO.update(category);
        LOGGER.info(finished, update);
        return category;
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(delete, id));
        if (categoryDAO.findCategoryOffers(id).size() != 0) {
            throw new EntityDeletingException(String.format("Fail to delete category with id %s. " +
                    "Some offers still referenced to it", id));
        }
        categoryDAO.delete(id);
        LOGGER.info(finished, String.format(delete, id));
    }

    @Transactional(readOnly = true)
    public List<Offer> findCategoryOffers(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findCategoryOffers, id));
        List<Offer> offers = categoryDAO.findCategoryOffers(id);
        LOGGER.info(finished, String.format(findCategoryOffers, id));
        return offers;
    }

    public Category addOffer(long categoryId, long offerId) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(addOfferToCategory, categoryId));
        Category category;
        try {
            category = categoryDAO.addOffer(categoryId, offerId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(String.format("Category with id %s doesn't exist", categoryId));
        }
        LOGGER.info(finished, String.format(addOfferToCategory, categoryId));
        return category;
    }

}

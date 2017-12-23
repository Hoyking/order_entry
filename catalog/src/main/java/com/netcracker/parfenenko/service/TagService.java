package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.TagDAO;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
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
public class TagService {

    private static final Logger LOGGER = LogManager.getLogger(TagService.class);

    private TagDAO tagDAO;

    private String started = "Operation of {} started";
    private String finished = "Operation of {} finished";

    private String save = "saving tag";
    private String findById = "searching for tag with id %s";
    private String findByName = "searching for tag with name %s";
    private String findAll = "searching for all prices";
    private String update = "updating a tag";
    private String delete = "deleting a tag with id %s";
    private String findTagOffers = "searching for offers which consist tag %s";


    @Autowired
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public Tag save(Tag tag) throws PersistenceMethodException {
        LOGGER.info(started, save);
        tag = tagDAO.save(tag);
        LOGGER.info(finished, save);
        return tag;
    }

    @Transactional(readOnly = true)
    public Tag findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findById, id));
        Tag tag = tagDAO.findById(id);
        LOGGER.info(finished, String.format(findById, id));
        return tag;
    }

    @Transactional(readOnly = true)
    public Tag findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findByName, name));
        Tag tag = tagDAO.findByName(name);
        LOGGER.info(finished, String.format(findByName, name));
        return tag;
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findAll);
        List<Tag> categories = tagDAO.findAll();
        LOGGER.info(finished, findAll);
        return categories;
    }

    public Tag update(Tag tag) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, update);
        tag = tagDAO.update(tag);
        LOGGER.info(finished, update);
        return tag;
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(delete, id));
        tagDAO.delete(id);
        LOGGER.info(finished, String.format(delete, id));
    }

    @Transactional(readOnly = true)
    public List<Offer> findTagOffers(String name) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findTagOffers, name));
        List<Offer> offers = tagDAO.findTagOffers(name);
        LOGGER.info(finished, String.format(findTagOffers, name));
        return offers;
    }
    
}

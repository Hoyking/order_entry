package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.TagDAO;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TagService {

    private TagDAO tagDAO;

    @Autowired
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public Tag save(Tag tag) throws PersistenceMethodException {
        return tagDAO.save(tag);
    }

    @Transactional(readOnly = true)
    public Tag findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        return tagDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Tag findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return tagDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll() throws PersistenceMethodException, EntityNotFoundException {
        return tagDAO.findAll();
    }

    public Tag update(Tag tag) throws PersistenceMethodException, EntityNotFoundException {
        return tagDAO.update(tag);
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        tagDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findTagOffers(String name) throws PersistenceMethodException, EntityNotFoundException {
        return tagDAO.findTagOffers(name);
    }
    
}

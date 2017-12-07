package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.TagDAO;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.entities.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService {

    private TagDAO tagDAO;

    @Autowired
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public Tag save(Tag tag) {
        return tagDAO.save(tag);
    }

    @Transactional(readOnly = true)
    public Tag findById(long id) {
        return tagDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Tag findByName(String name) {
        return tagDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    public Tag update(Tag tag) {
        return tagDAO.update(tag);
    }

    public void delete(long id) {
        tagDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findTagOffers(String name) {
        return tagDAO.findTagOffers(name);
    }
    
}

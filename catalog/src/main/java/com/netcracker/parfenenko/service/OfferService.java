package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.filter.OfferFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OfferService {

    private OfferDAO offerDAO;

    @Autowired
    public OfferService(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

    public Offer save(Offer offer) throws PersistenceMethodException {
        return offerDAO.save(offer);
    }

    @Transactional(readOnly = true)
    public Offer findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Offer findByName(String name) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Offer> findAll() throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.findAll();
    }

    public Offer update(Offer offer) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.update(offer);
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        offerDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findByFilter(OfferFilter offerFilter) throws PersistenceMethodException,
            EntityNotFoundException {
        return offerDAO.findByFilters(offerFilter);
    }

    @Transactional(readOnly = true)
    public Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.findTags(offerId);
    }

    public Offer changeAvailability(long id) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.changeAvailability(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersByTags(List<String> tags) throws PersistenceMethodException, EntityNotFoundException {
        List<Tag> tagList = tags.stream().map(this::createTag).collect(Collectors.toList());
        return offerDAO.findOffersByTags(tagList);
    }

    @Transactional(readOnly = true)
    public List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.findAvailableOffers();
    }

    public Offer addPriceToOffer(long id, Price price) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.addPriceToOffer(id, price);
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException {
        return offerDAO.findOffersOfPriceInterval(fromPrice, toPrice);
    }

    public Offer addTagToOffer(long id, Tag tag) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.addTagToOffer(id, tag);
    }

    public Offer removeTagFromOffer(long id, Tag tag) throws PersistenceMethodException, EntityNotFoundException {
        return offerDAO.removeTagFromOffer(id, tag);
    }

    private Tag createTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        return tag;
    }

}

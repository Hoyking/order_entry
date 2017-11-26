package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {

    private OfferDAO offerDAO;

    @Autowired
    public OfferService(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

    @Transactional
    public Offer save(Offer offer) {
        return offerDAO.save(offer);
    }

    @Transactional(readOnly = true)
    public Offer findById(long id) {
        return offerDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Offer findByName(String name) {
        return offerDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Offer> findAll() {
        return offerDAO.findAll();
    }

    @Transactional
    public Offer update(Offer offer) {
        return offerDAO.update(offer);
    }

    @Transactional
    public void delete(long id) {
        offerDAO.delete(id);
    }

    @Transactional
    public Offer changeAvailability(long id) {
        return offerDAO.changeAvailability(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersByTags(List<Tag> tags) {
        return offerDAO.findOffersByTags(tags);
    }

    @Transactional(readOnly = true)
    public List<Offer> findAvailableOffers() {
        return offerDAO.findAvailableOffers();
    }

    @Transactional
    public Offer addPriceToOffer(long id, Price price) {
        return offerDAO.addPriceToOffer(id, price);
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) {
        return offerDAO.findOffersOfPriceInterval(fromPrice, toPrice);
    }

    @Transactional
    public Offer addTagToOffer(long id, Tag tag) {
        return offerDAO.addTagToOffer(id, tag);
    }

    @Transactional
    public Offer removeTagFromOffer(long id, Tag tag) {
        return offerDAO.removeTagFromOffer(id, tag);
    }

}

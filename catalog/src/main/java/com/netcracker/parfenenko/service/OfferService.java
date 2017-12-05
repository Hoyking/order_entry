package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.config.AppConfig;
import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OfferService {

    private OfferDAO offerDAO;

    @Autowired
    public OfferService(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

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

    public Offer update(Offer offer) {
        return offerDAO.update(offer);
    }

    public void delete(long id) {
        offerDAO.delete(id);
    }

    public Offer changeAvailability(long id) {
        return offerDAO.changeAvailability(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersByTags(List<String> tags) {
        List<Tag> tagList = tags.stream().map(this::createTag).collect(Collectors.toList());
        return offerDAO.findOffersByTags(tagList);
    }

    @Transactional(readOnly = true)
    public List<Offer> findAvailableOffers() {
        return offerDAO.findAvailableOffers();
    }

    public Offer addPriceToOffer(long id, Price price) {
        return offerDAO.addPriceToOffer(id, price);
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) {
        return offerDAO.findOffersOfPriceInterval(fromPrice, toPrice);
    }

    public Offer addTagToOffer(long id, Tag tag) {
        return offerDAO.addTagToOffer(id, tag);
    }

    public Offer removeTagFromOffer(long id, Tag tag) {
        return offerDAO.removeTagFromOffer(id, tag);
    }

    private Tag createTag(String tagName) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Tag tag = context.getBean(Tag.class);
        tag.setName(tagName);
        return tag;
    }

}

package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        offer.setTags(findTags(offer.getId()));
        return offerDAO.update(offer);
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        offerDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Offer> findByFilter(Map<String, List<String>> offerFilter) throws PersistenceMethodException,
            EntityNotFoundException, IllegalArgumentException {
        filtersValidation(offerFilter);
        List<String> categories = offerFilter.get("categories");
        List<Long> categoriesId = null;
        if (categories != null) {
            categoriesId = parseLong(categories);
        }
        List<String> tags = offerFilter.get("tags");
        List<String> price = offerFilter.get("price");
        double from = Double.parseDouble(price.get(0));
        double to = Double.parseDouble(price.get(1));
        return offerDAO.findByFilters(categoriesId, tags, from, to);
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

    private void filtersValidation(Map<String, List<String>> filters) {
        for (String key: filters.keySet()) {
            if (!key.equals("categories") && !key.equals("tags") && !key.equals("price")) {
                throw new IllegalArgumentException("Wrong filters");
            }
        }

        List<String> price = filters.get("price");
        if (price == null) {
            price = new ArrayList<>(2);
            price.add("0");
            price.add("0");
            filters.put("price", price);
        } else if (price.size() != 2) {
            throw new IllegalArgumentException("Wrong filters");
        } else {
            String fromValueString = price.get(0);
            String toValueString = price.get(1);
            try {
                double from = Double.parseDouble(fromValueString);
                double to = Double.parseDouble(toValueString);
                if (from < 0 || to < 0 || from > to) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Wrong filters");
            }
        }
    }

    private List<Long> parseLong(List<String> stringList) throws IllegalArgumentException {
        List<Long> longList = new ArrayList<>(stringList.size());
        try {
            for (String string: stringList) {
                longList.add(Long.parseLong(string));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        return longList;
    }

}

package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entity.Category;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Price;
import com.netcracker.parfenenko.entity.Tag;
import com.netcracker.parfenenko.exception.EntityCreationException;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("FieldCanBeLocal")
@Service
@Transactional(rollbackFor = Exception.class)
public class OfferService {

    private static final Logger LOGGER = LogManager.getLogger(OfferService.class);

    private OfferDAO offerDAO;
    private CategoryDAO categoryDAO;
    private PriceDAO priceDAO;

    private final String STARTED = "Operation of {} STARTED";
    private final String FINISHED = "Operation of {} FINISHED";

    private final String SAVE = "saving offer";
    private final String FIND_BY_ID = "searching for offer with id %s";
    private final String FIND_BY_NAME = "searching for offer with name %s";
    private final String FIND_BY_PART_OF_NAME = "searching for offers which name consist %s";
    private final String FIND_ALL = "searching for all offers";
    private final String UPDATE = "updating an offer";
    private final String DELETE = "deleting an offer with id %s";
    private final String FIND_BY_FILTER = "searching for offers by filters";
    private final String FIND_TAGS = "searching for tags of the offer with id %s";
    private final String CHANGE_AVAILABILITY = "changing availability of the offer with id %s";
    private final String FIND_BY_TAGS = "searching for offers by tags";
    private final String FIND_AVAILABLE_OFFERS = "searching for available offers";
    private final String UPDATE_PRICE = "updating price of the offer with id %s";
    private final String FIND_OFFERS_OF_PRICE_INTERVAL = "searching for offers of price interval (%s, %s)";
    private final String ADD_TAG_TO_OFFER = "adding tag to the offer with id %s";
    private final String REMOVE_TAG_FROM_OFFER = "removing tag from the offer with id %s";

    @Autowired
    public OfferService(OfferDAO offerDAO, CategoryDAO categoryDAO, PriceDAO priceDAO) {
        this.offerDAO = offerDAO;
        this.categoryDAO = categoryDAO;
        this.priceDAO = priceDAO;
    }

    @SuppressWarnings("Duplicates")
    public Offer save(Offer offer) throws PersistenceMethodException {
        LOGGER.info(STARTED, SAVE);
        try {
            Category category = categoryDAO.findById(offer.getCategory().getId());
            offer.setCategory(category);
        } catch (EntityNotFoundException | NullPointerException e) {
            throw new IllegalArgumentException("Wrong category");
        }
        try {
            offerDAO.findByName(offer.getName());
        } catch (EntityNotFoundException e) {
            offer = offerDAO.save(offer);
            LOGGER.info(FINISHED, SAVE);
            return offer;
        }
        throw new EntityCreationException("Offer with such name already exists");
    }

    @Transactional(readOnly = true)
    public Offer findById(long id) throws PersistenceMethodException, EntityNotFoundException, NoContentException {
        LOGGER.info(STARTED, String.format(FIND_BY_ID, id));
        Offer offer;
        try {
            offer = offerDAO.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.info(FINISHED, String.format(FIND_BY_ID, id));
            throw new NoContentException();
        }
        LOGGER.info(FINISHED, String.format(FIND_BY_ID, id));
        return offer;
    }

    @Transactional(readOnly = true)
    public Offer findByName(String name) throws PersistenceMethodException, EntityNotFoundException, NoContentException {
        LOGGER.info(STARTED, String.format(FIND_BY_NAME, name));
        Offer offer;
        try {
            offer = offerDAO.findByName(name);
        } catch (EntityNotFoundException e) {
            LOGGER.info(FINISHED, String.format(FIND_BY_NAME, name));
            throw new NoContentException();
        }
        LOGGER.info(FINISHED, String.format(FIND_BY_NAME, name));
        return offer;
    }

    @Transactional(readOnly = true)
    public List<Offer> findByPartOfName(String part) throws PersistenceMethodException {
        LOGGER.info(STARTED, String.format(FIND_BY_PART_OF_NAME, part));
        List<Offer> offers = offerDAO.findByPartOfName(part);
        LOGGER.info(FINISHED, String.format(FIND_BY_PART_OF_NAME, part));
        return offers;
    }

    @Transactional(readOnly = true)
    public List<Offer> findAll() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, FIND_ALL);
        List<Offer> offers = offerDAO.findAll();
        LOGGER.info(FINISHED, FIND_ALL);
        return offers;
    }

    public Offer update(Offer offer) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, UPDATE);
        try {
            Category category = categoryDAO.findById(offer.getCategory().getId());
            offer.setCategory(category);
        } catch (EntityNotFoundException | NullPointerException e) {
            throw new IllegalArgumentException("Wrong category");
        }
        offer.setTags(offerDAO.findTags(offer.getId()));
        offer = offerDAO.update(offer);
        LOGGER.info(FINISHED, UPDATE);
        return offer;
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, String.format(DELETE, id));
        offerDAO.delete(id);
        LOGGER.info(FINISHED, String.format(DELETE, id));
    }

    @Transactional(readOnly = true)
    public List<Offer> findByFilter(Map<String, List<String>> offerFilter) throws PersistenceMethodException,
            EntityNotFoundException, IllegalArgumentException {
        LOGGER.info(STARTED, FIND_BY_FILTER);
        List<Offer> offers;
        Double[] price = filtersValidation(offerFilter);
        Double from = price[0];
        Double to = price[1];

        List<String> categories = offerFilter.get("categories");
        List<Long> categoriesId = null;
        if (categories != null) {
            categoriesId = parseLong(categories);
        }

        List<String> tags = offerFilter.get("tags");

        offers = offerDAO.findByFilters(categoriesId, tags, from, to);
        LOGGER.info(FINISHED, FIND_BY_FILTER);
        return offers;
    }

    @Transactional(readOnly = true)
    public Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, String.format(FIND_TAGS, offerId));
        Set<Tag> tags = offerDAO.findTags(offerId);
        LOGGER.info(FINISHED, String.format(FIND_TAGS, offerId));
        return tags;
    }

    public Offer changeAvailability(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, String.format(CHANGE_AVAILABILITY, id));
        Offer offer = offerDAO.findById(id);
        offer.setAvailable(!offer.isAvailable());
        offer = offerDAO.update(offer);
        LOGGER.info(FINISHED, String.format(CHANGE_AVAILABILITY, id));
        return offer;
    }

    @Transactional(readOnly = true)
    public List<Offer> findByTags(List<String> tags) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, FIND_BY_TAGS);
        List<Offer> offers = offerDAO.findByTags(tags);
        LOGGER.info(FINISHED, FIND_BY_TAGS);
        return offers;
    }

    @Transactional(readOnly = true)
    public List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, FIND_AVAILABLE_OFFERS);
        List<Offer> offers = offerDAO.findAvailableOffers();
        LOGGER.info(FINISHED, FIND_AVAILABLE_OFFERS);
        return offers;
    }

    public Offer updatePrice(long id, double value) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, String.format(UPDATE_PRICE, id));
        Offer offer = offerDAO.findById(id);
        Price currentPrice = offer.getPrice();
        Price price = new Price();
        price.setValue(value);
        offer.setPrice(price);
        offer = offerDAO.update(offer);
        priceDAO.delete(currentPrice.getId());
        LOGGER.info(FINISHED, String.format(UPDATE_PRICE, id));
        return offer;
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException, IllegalArgumentException {
        LOGGER.info(STARTED, String.format(FIND_OFFERS_OF_PRICE_INTERVAL, fromPrice, toPrice));
        try {
            priceValueValidation(fromPrice, toPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        List<Offer> offers = offerDAO.findOffersOfPriceInterval(fromPrice, toPrice);
        LOGGER.info(FINISHED, String.format(FIND_OFFERS_OF_PRICE_INTERVAL, fromPrice, toPrice));
        return offers;
    }

    public Offer addTagToOffer(long id, String tagName) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, String.format(ADD_TAG_TO_OFFER, id));
        Offer offer = offerDAO.findById(id);
        Tag tag = new Tag();
        tag.setName(tagName);
        offer.getTags().add(tag);
        offer = offerDAO.update(offer);
        LOGGER.info(FINISHED, String.format(ADD_TAG_TO_OFFER, id));
        return offer;
    }

    public Offer removeTagFromOffer(long id, String tagName) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(STARTED, String.format(REMOVE_TAG_FROM_OFFER, id));
        Offer offer = offerDAO.findById(id);
        Tag tag = new Tag();
        tag.setName(tagName);
        offer.getTags().remove(tag);
        offer = offerDAO.update(offer);
        LOGGER.info(FINISHED, String.format(REMOVE_TAG_FROM_OFFER, id));
        return offer;
    }

    private Double[] filtersValidation(Map<String, List<String>> filters) throws IllegalArgumentException {
        for (String key : filters.keySet()) {
            if (!key.equals("categories") && !key.equals("tags") && !key.equals("from") && !key.equals("to")) {
                throw new IllegalArgumentException("Wrong filters");
            }
        }

        List<String> fromList = filters.get("from");
        List<String> toList = filters.get("to");
        Double from = null;
        Double to = null;

        if (fromList != null) {
            if (fromList.size() != 1) {
                throw new IllegalArgumentException("Wrong filters");
            } else {
                from = Double.parseDouble(fromList.get(0));
                if (from < 0) {
                    throw new IllegalArgumentException("From price should not be less then 0");
                }
            }
        }
        if (toList != null) {
            if (toList.size() != 1) {
                throw new IllegalArgumentException("Wrong filters");
            } else {
                to = Double.parseDouble(toList.get(0));
                if (to < 0) {
                    throw new IllegalArgumentException("To price should not be less then 0");
                }
            }
        }
        if (from != null && to != null) {
            if (to < from) {
                throw new IllegalArgumentException("To price should not be less then from price");
            }
        }

        return new Double[] {from, to};
    }

    private void priceValueValidation(double from, double to) throws NumberFormatException {
        if (from < 0 || to < 0 || from > to) {
            throw new NumberFormatException("Wrong price format");
        }
    }

    private List<Long> parseLong(List<String> stringList) throws IllegalArgumentException {
        List<Long> longList = new ArrayList<>(stringList.size());
        try {
            for (String string : stringList) {
                longList.add(Long.parseLong(string));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        return longList;
    }

}

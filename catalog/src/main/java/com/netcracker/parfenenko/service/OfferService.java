package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
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

    private String started = "Operation of {} started";
    private String finished = "Operation of {} finished";

    private String save = "saving offer";
    private String findById = "searching for offer with id %s";
    private String findByName = "searching for offer with name %s";
    private String findByPartOfName = "searching for offers which name consist %s";
    private String findAll = "searching for all offers";
    private String update = "updating an offer";
    private String delete = "deleting an offer with id %s";
    private String findByFilter = "searching for offers by filters";
    private String findTags = "searching for tags of the offer with id %s";
    private String changeAvailability = "changing availability of the offer with id %s";
    private String findByTags = "searching for offers by tags";
    private String findAvailableOffers = "searching for available offers";
    private String updatePrice = "updating price of the offer with id %s";
    private String findOffersOfPriceInterval = "searching for offers of price interval (%s, %s)";
    private String addTagToOffer = "adding tag to the offer with id %s";
    private String removeTagFromOffer = "removing tag from the offer with id %s";

    @Autowired
    public OfferService(OfferDAO offerDAO, CategoryDAO categoryDAO, PriceDAO priceDAO) {
        this.offerDAO = offerDAO;
        this.categoryDAO = categoryDAO;
        this.priceDAO = priceDAO;
    }

    @SuppressWarnings("Duplicates")
    public Offer save(Offer offer) throws PersistenceMethodException {
        LOGGER.info(started, save);
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
            LOGGER.info(finished, save);
            return offer;
        }
        throw new EntityCreationException("Offer with such name already exists");
    }

    @Transactional(readOnly = true)
    public Offer findById(long id) throws PersistenceMethodException, EntityNotFoundException, NoContentException {
        LOGGER.info(started, String.format(findById, id));
        Offer offer;
        try {
            offer = offerDAO.findById(id);
        } catch (EntityNotFoundException e) {
            LOGGER.info(finished, String.format(findById, id));
            throw new NoContentException();
        }
        LOGGER.info(finished, String.format(findById, id));
        return offer;
    }

    @Transactional(readOnly = true)
    public Offer findByName(String name) throws PersistenceMethodException, EntityNotFoundException, NoContentException {
        LOGGER.info(started, String.format(findByName, name));
        Offer offer;
        try {
            offer = offerDAO.findByName(name);
        } catch (EntityNotFoundException e) {
            LOGGER.info(finished, String.format(findByName, name));
            throw new NoContentException();
        }
        LOGGER.info(finished, String.format(findByName, name));
        return offer;
    }

    @Transactional(readOnly = true)
    public List<Offer> findByPartOfName(String part) throws PersistenceMethodException {
        LOGGER.info(started, String.format(findByPartOfName, part));
        List<Offer> offers = offerDAO.findByPartOfName(part);
        LOGGER.info(finished, String.format(findByPartOfName, part));
        return offers;
    }

    @Transactional(readOnly = true)
    public List<Offer> findAll() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findAll);
        List<Offer> offers = offerDAO.findAll();
        LOGGER.info(finished, findAll);
        return offers;
    }

    public Offer update(Offer offer) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, update);
        try {
            Category category = categoryDAO.findById(offer.getCategory().getId());
            offer.setCategory(category);
        } catch (EntityNotFoundException | NullPointerException e) {
            throw new IllegalArgumentException("Wrong category");
        }
        offer = offerDAO.update(offer);
        LOGGER.info(finished, update);
        return offer;
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(delete, id));
        offerDAO.delete(id);
        LOGGER.info(finished, String.format(delete, id));
    }

    @Transactional(readOnly = true)
    public List<Offer> findByFilter(Map<String, List<String>> offerFilter) throws PersistenceMethodException,
            EntityNotFoundException, IllegalArgumentException {
        LOGGER.info(started, findByFilter);
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
        LOGGER.info(started, findByFilter);
        return offers;
    }

    @Transactional(readOnly = true)
    public Set<Tag> findTags(long offerId) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findTags, offerId));
        Set<Tag> tags = offerDAO.findTags(offerId);
        LOGGER.info(finished, String.format(findTags, offerId));
        return tags;
    }

    public Offer changeAvailability(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(changeAvailability, id));
        Offer offer = offerDAO.findById(id);
        offer.setAvailable(!offer.isAvailable());
        offer = offerDAO.update(offer);
        LOGGER.info(finished, String.format(changeAvailability, id));
        return offer;
    }

    @Transactional(readOnly = true)
    public List<Offer> findByTags(List<String> tags) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findByTags);
        List<Offer> offers = offerDAO.findByTags(tags);
        LOGGER.info(finished, findByTags);
        return offers;
    }

    @Transactional(readOnly = true)
    public List<Offer> findAvailableOffers() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findAvailableOffers);
        List<Offer> offers = offerDAO.findAvailableOffers();
        LOGGER.info(finished, findAvailableOffers);
        return offers;
    }

    public Offer updatePrice(long id, double value) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(updatePrice, id));
        Offer offer = offerDAO.findById(id);
        Price currentPrice = offer.getPrice();
        Price price = new Price();
        price.setValue(value);
        offer.setPrice(price);
        offer = offerDAO.update(offer);
        priceDAO.delete(currentPrice.getId());
        LOGGER.info(finished, String.format(updatePrice, id));
        return offer;
    }

    @Transactional(readOnly = true)
    public List<Offer> findOffersOfPriceInterval(double fromPrice, double toPrice) throws PersistenceMethodException,
            EntityNotFoundException, IllegalArgumentException {
        LOGGER.info(started, String.format(findOffersOfPriceInterval, fromPrice, toPrice));
        try {
            priceValueValidation(fromPrice, toPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        List<Offer> offers = offerDAO.findOffersOfPriceInterval(fromPrice, toPrice);
        LOGGER.info(finished, String.format(findOffersOfPriceInterval, fromPrice, toPrice));
        return offers;
    }

    public Offer addTagToOffer(long id, String tagName) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(addTagToOffer, id));
        Offer offer = offerDAO.findById(id);
        Tag tag = new Tag();
        tag.setName(tagName);
        offer.getTags().add(tag);
        offer = offerDAO.update(offer);
        LOGGER.info(finished, String.format(addTagToOffer, id));
        return offer;
    }

    public Offer removeTagFromOffer(long id, String tagName) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(removeTagFromOffer, id));
        Offer offer = offerDAO.findById(id);
        Tag tag = new Tag();
        tag.setName(tagName);
        offer.getTags().remove(tag);
        offer = offerDAO.update(offer);
        LOGGER.info(finished, String.format(removeTagFromOffer, id));
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
            }
        }
        if (toList != null) {
            if (toList.size() != 1) {
                throw new IllegalArgumentException("Wrong filters");
            } else {
                to = Double.parseDouble(toList.get(0));
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

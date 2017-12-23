package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entities.Price;
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
public class PriceService {

    private static final Logger LOGGER = LogManager.getLogger(PriceService.class);

    private PriceDAO priceDAO;

    private String started = "Operation of {} started";
    private String finished = "Operation of {} finished";

    private String save = "saving price";
    private String findById = "searching for price with id %s";
    private String findAll = "searching for all prices";
    private String update = "updating a price";
    private String delete = "deleting a price with id %s";

    @Autowired
    public PriceService(PriceDAO priceDAO) {
        this.priceDAO = priceDAO;
    }

    public Price save(Price price) throws PersistenceMethodException {
        LOGGER.info(started, save);
        price = priceDAO.save(price);
        LOGGER.info(finished, save);
        return price;
    }

    @Transactional(readOnly = true)
    public Price findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(findById, id));
        Price price = priceDAO.findById(id);
        LOGGER.info(finished, String.format(findById, id));
        return price;
    }

    @Transactional(readOnly = true)
    public List<Price> findAll() throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, findAll);
        List<Price> categories = priceDAO.findAll();
        LOGGER.info(finished, findAll);
        return categories;
    }

    public Price update(Price price) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, update);
        price = priceDAO.update(price);
        LOGGER.info(finished, update);
        return price;
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        LOGGER.info(started, String.format(delete, id));
        priceDAO.delete(id);
        LOGGER.info(finished, String.format(delete, id));
    }
    
}

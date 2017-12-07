package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class PriceService {

    private PriceDAO priceDAO;

    @Autowired
    public PriceService(PriceDAO priceDAO) {
        this.priceDAO = priceDAO;
    }

    public Price save(Price price) throws PersistenceMethodException {
        return priceDAO.save(price);
    }

    @Transactional(readOnly = true)
    public Price findById(long id) throws PersistenceMethodException, EntityNotFoundException {
        return priceDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Price> findAll() throws PersistenceMethodException, EntityNotFoundException {
        return priceDAO.findAll();
    }

    public Price update(Price price) throws PersistenceMethodException, EntityNotFoundException {
        return priceDAO.update(price);
    }

    public void delete(long id) throws PersistenceMethodException, EntityNotFoundException {
        priceDAO.delete(id);
    }
    
}

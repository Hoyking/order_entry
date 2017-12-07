package com.netcracker.parfenenko.service;

import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PriceService {

    private PriceDAO priceDAO;

    @Autowired
    public PriceService(PriceDAO priceDAO) {
        this.priceDAO = priceDAO;
    }

    public Price save(Price price) {
        return priceDAO.save(price);
    }

    @Transactional(readOnly = true)
    public Price findById(long id) {
        return priceDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Price> findAll() {
        return priceDAO.findAll();
    }

    public Price update(Price price) {
        return priceDAO.update(price);
    }

    public void delete(long id) {
        priceDAO.delete(id);
    }
    
}

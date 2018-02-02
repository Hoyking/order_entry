package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entity.Price;
import org.springframework.stereotype.Repository;

@Repository
public class JPAPriceDAO extends JPAGenericDAO<Price, Long> implements PriceDAO {

    public JPAPriceDAO() {
        super.setPersistenceClass(Price.class);
    }

}

package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.InventoryOrder;
import org.springframework.stereotype.Repository;

@Repository
public class JPAOrderDAO extends JPANamedEntityDAO<InventoryOrder, Long> implements OrderDAO {

    public JPAOrderDAO() {
        super.setPersistenceClass(InventoryOrder.class);
    }

}

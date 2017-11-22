package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.InventoryOrder;
import com.netcracker.parfenenko.entities.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public class JPAOrderDAO extends JPANamedEntityDAO<InventoryOrder, Long> implements OrderDAO {

    public JPAOrderDAO() {
        super.setPersistenceClass(InventoryOrder.class);
    }

    @Override
    public InventoryOrder addOrderItem(long orderId, OrderItem orderItem) {
        InventoryOrder order = findById(orderId);
        order.getOrderItems().add(orderItem);
        return update(order);
    }

    @Override
    public InventoryOrder removeOrderItem(long orderId, OrderItem orderItem) {
        InventoryOrder order = findById(orderId);
        order.getOrderItems().remove(orderItem);
        return update(order);
    }

}

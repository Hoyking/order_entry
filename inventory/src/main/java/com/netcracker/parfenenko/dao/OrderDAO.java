package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.InventoryOrder;
import com.netcracker.parfenenko.entities.OrderItem;

public interface OrderDAO extends NamedEntityDAO<InventoryOrder, Long> {

    InventoryOrder addOrderItem(long orderId, OrderItem orderItem);

    InventoryOrder removeOrderItem(long orderId, OrderItem orderItem);

}

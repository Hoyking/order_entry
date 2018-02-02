package com.netcracker.parfenenko.repository;

import com.netcracker.parfenenko.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, Long> {

}

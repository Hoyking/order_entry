package com.netcracker.parfenenko.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class MongoTemplateOrderRepository implements OrderRepository {

    private MongoTemplate mongoTemplate;

    @Autowired
    public MongoTemplateOrderRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Order save(Order order) {
        mongoTemplate.insert(order);
        return order;
    }

    @Override
    public Order findById(String id) {
        return mongoTemplate.findById(id, Order.class);
    }

    @Override
    public List<Order> findAll() {
        return mongoTemplate.findAll(Order.class);
    }

    @Override
    public Order update(Order order) {
        mongoTemplate.save(order);
        return order;
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(findById(id));
    }

    @Override
    public Order findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Order.class);
    }

    @Override
    public Set<OrderItem> findOrderItems(String orderId) {
        Query query = new Query(Criteria
                .where("_id").is(orderId)
        );
        query.fields().include("orderItems");
        Order order = mongoTemplate.findOne(query, Order.class);
        System.out.println(order.getOrderItems());
        return order.getOrderItems();
    }

    @Override
    public Order removeOrderItem(String orderId, String orderItemId) {
        Query query = new Query(Criteria
                .where("_id").is(orderId)
                .and("orderItems.id").is(orderItemId));
        DBObject dbObject = BasicDBObjectBuilder
                .start()
                .add("id", orderItemId)
                .get();
        Update update = new Update().pull("orderItems", dbObject);
        mongoTemplate.updateFirst(query, update, Order.class);
        return findById(orderId);
    }

    @Override
    public List<Order> findOrdersByPaymentStatus(String paymentStatus) {
        Query query = new Query(Criteria
                .where("paymentStatus").is(paymentStatus));
        return mongoTemplate.find(query, Order.class);
    }

}

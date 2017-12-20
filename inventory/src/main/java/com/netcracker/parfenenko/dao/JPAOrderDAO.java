package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.UpdateStatusException;
import com.netcracker.parfenenko.exception.StatusSignException;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.util.Statuses;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class JPAOrderDAO extends JPANamedEntityDAO<Order, Long> implements OrderDAO {

    public JPAOrderDAO() {
        super.setPersistenceClass(Order.class);
    }

    @Override
    public Set<OrderItem> findOrderItems(long orderId) throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(entityManager ->
            new HashSet<>(entityManager
                    .createNamedQuery("findOrderItems")
                    .setParameter(1, orderId)
                    .getResultList()));
    }

    @Override
    public Order addOrderItem(long orderId, OrderItem orderItem) throws PersistenceMethodException, EntityNotFoundException {
        Order order = findById(orderId);
        orderItem.setId(0);
        order.getOrderItems().add(orderItem);
        return update(order);
    }

    @Override
    public Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(entityManager ->
                removeOrderItemQuery(entityManager, orderId, orderItemId));
    }

    @Override
    public List<Order> findOrdersByPaymentStatus(int paymentStatus) throws StatusSignException, PersistenceMethodException,
            EntityNotFoundException{
        if (!Statuses.consists(paymentStatus)) {
            throw new StatusSignException();
        }
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> ordersWithPaymentStatusCriteriaQuery(entityManager, paymentStatus));
    }

    @Override
    public Order updateStatus(long id, int status) throws UpdateStatusException, StatusSignException,
            PersistenceMethodException, EntityNotFoundException {
        Order order = findById(id);
        isValid(order.getPaymentStatus(), status);
        order.setPaymentStatus(status);
        try {
            order = update(order);
        } catch (PersistenceMethodException e) {
            order.setPaymentStatus(Statuses.REJECTED_PAYMENT.value());
            update(order);
            throw new UpdateStatusException("Fail to update status of the order with id " + id);
        }
        return order;
    }

    private void isValid(int currentStatus, int newStatus) throws StatusSignException {
        if (!Statuses.consists(newStatus)) {
            throw new StatusSignException();
        }

        final int OPENED = Statuses.OPENED.value();
        final int PAID = Statuses.PAID.value();
        final int CANCELED = Statuses.CANCELED.value();
        final int REJECTED = Statuses.REJECTED_PAYMENT.value();

        final String OPENED_MESSAGE = "Opened order can be changed either to paid or canceled";
        final String REJECTED_MESSAGE = "Rejected payment can be changed either to paid or canceled";
        final String CANCELED_MESSAGE = "Canceled order can't be changed";
        final String PAID_MESSAGE = "Paid order can't be changed";

        if (currentStatus == OPENED) {
            if (newStatus == OPENED || newStatus == REJECTED) {
                throw new StatusSignException(OPENED_MESSAGE);
            }
        }
        if (currentStatus == REJECTED) {
            if (newStatus == OPENED || newStatus == REJECTED) {
                throw new StatusSignException(REJECTED_MESSAGE);
            }
        }
        if (currentStatus == PAID) {
            throw new StatusSignException(PAID_MESSAGE);
        }
        if (currentStatus == CANCELED) {
            throw new StatusSignException(CANCELED_MESSAGE);
        }
    }

    private List<Order> ordersWithPaymentStatusCriteriaQuery(EntityManager entityManager, int paymentStatus) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        ParameterExpression<Integer> parameter = criteriaBuilder.parameter(Integer.class);
        query.select(root).where(criteriaBuilder.equal(root.get("paymentStatus"), parameter));

        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setParameter(parameter, paymentStatus);
        return typedQuery.getResultList();
    }

    private Order removeOrderItemQuery(EntityManager entityManager, long orderId, long orderItemId) {
        Order order = null;
        try {
            order = findById(orderId);
        } catch (PersistenceMethodException e) {
            e.printStackTrace();
        }
        OrderItem orderItem = entityManager
                .createNamedQuery("findOrderItem", OrderItem.class)
                .setParameter(1, orderItemId)
                .getSingleResult();
        order.getOrderItems().remove(orderItem);
        try {
            order = update(order);
        } catch (PersistenceMethodException e) {
            e.printStackTrace();
        }
        return order;
    }

}

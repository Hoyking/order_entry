package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PersistenceMethodException;
import com.netcracker.parfenenko.exception.StatusSignException;
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

@SuppressWarnings("unchecked")
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
                        .getResultList())
        );
    }

    @Override
    public Order removeOrderItem(long orderId, long orderItemId) throws PersistenceMethodException, EntityNotFoundException {
        return persistenceMethodsProvider.functionalMethod(entityManager ->
                removeOrderItemQuery(entityManager, orderId, orderItemId));
    }

    @Override
    public List<Order> findOrdersByPaymentStatus(int paymentStatus) throws StatusSignException, PersistenceMethodException,
            EntityNotFoundException {
        return persistenceMethodsProvider
                .functionalMethod(entityManager -> ordersWithPaymentStatusCriteriaQuery(entityManager, paymentStatus));
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

    private Order removeOrderItemQuery(EntityManager entityManager, long orderId, long orderItemId)
            throws PersistenceMethodException, EntityNotFoundException {
        Order order = findById(orderId);
        OrderItem orderItem = entityManager
                .createNamedQuery("findOrderItem", OrderItem.class)
                .setParameter(1, orderItemId)
                .getSingleResult();
        order.getOrderItems().remove(orderItem);
        order = update(order);
        return order;
    }

}

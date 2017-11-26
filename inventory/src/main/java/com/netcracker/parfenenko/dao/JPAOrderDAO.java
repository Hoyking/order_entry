package com.netcracker.parfenenko.dao;

import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import com.netcracker.parfenenko.util.Payments;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class JPAOrderDAO extends JPANamedEntityDAO<Order, Long> implements OrderDAO {

    public JPAOrderDAO() {
        super.setPersistenceClass(Order.class);
    }

    @Override
    public Order addOrderItem(long orderId, OrderItem orderItem) {
        Order order = findById(orderId);
        orderItem.setId(0);
        order.getOrderItems().add(orderItem);
        return update(order);
    }

    @Override
    public Order removeOrderItem(long orderId, OrderItem orderItem) {
        Order order = findById(orderId);
        order.getOrderItems().remove(orderItem);
        return update(order);
    }

    @Override
    public List<Order> findOrdersByPaymentStatus(int paymentStatus) throws PaymentStatusException {
        if (!Payments.consists(paymentStatus)) {
            throw new PaymentStatusException();
        }
        return transactions.startGenericTransaction(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            ParameterExpression<Integer> parameter = criteriaBuilder.parameter(Integer.class);
            query.select(root).where(criteriaBuilder.equal(root.get("paymentStatus"), parameter));

            TypedQuery<Order>  typedQuery = entityManager.createQuery(query);
            typedQuery.setParameter(parameter, paymentStatus);
            return typedQuery.getResultList();
        });
    }

    @Override
    public Order payForOrder(long id) throws PayForOrderException {
        Order order = findById(id);
        if (order.getPaymentStatus() == Payments.PAID.value()) {
            throw new PayForOrderException("Order with id " + id + " is already paid for");
        }
        order.setPaymentStatus(Payments.PAID.value());
        order = update(order);
        if(order.getPaymentStatus() != Payments.PAID.value()) {
            order.setPaymentStatus(Payments.REJECTED.value());
            update(order);
            throw new PayForOrderException("Fail to pay for order with id " + id);
        }
        return order;
    }

}

import com.netcracker.parfenenko.Application;
import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.exception.PayForOrderException;
import com.netcracker.parfenenko.exception.PaymentStatusException;
import com.netcracker.parfenenko.service.OrderService;
import com.netcracker.parfenenko.util.Payments;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderTest {

    @Autowired
    private OrderService orderService;

    private long orderId;
    private final String NAME_1 = "Test Order 1";
    private final String NAME_2 = "Test Order 2";
    private final String NAME_3 = "Updated test Order 1";
    private final String DESCRIPTION_1 = "Test description 1";
    private final String DESCRIPTION_2 = "Test description 2";
    private final String DESCRIPTION_3 = "Updated test description 1";
    private final double TOTAL_PRICE = 0.99;
    private final double UPDATED_TOTAL_PRICE = 1.99;
    private final String MAIL = "Test mail";
    private final String DATE = "Test date";
    private final String SIGN = "Test sign";

    private final String ORDER_ITEM_NAME_1 = "Test OrderItem 1";
    private final String ORDER_ITEM_NAME_2 = "Test OrderItem 2";
    private final String ORDER_ITEM_NAME_3 = "Test OrderItem 3";
    private final String ORDER_ITEM_DESCRIPTION_3 = "Test OrderItem description 3";
    private final String ORDER_ITEM_DESCRIPTION_1 = "Test OrderItem description 1";
    private final String ORDER_ITEM_DESCRIPTION_2 = "Test OrderItem description 2";

    @Before
    public void initOrder() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName(ORDER_ITEM_NAME_2);
        orderItem2.setDescription(ORDER_ITEM_DESCRIPTION_2);

        Order order = new Order();
        order.setName(NAME_1);
        order.setDescription(DESCRIPTION_1);
        order.setTotalPrice(TOTAL_PRICE);
        order.setCustomerMail(MAIL);
        order.setOrderDate(DATE);
        order.setPaymentSign(SIGN);
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        order = orderService.save(order);
        orderId = order.getId();
    }

    @After
    public void destroyOrder() {
        orderService.delete(orderId);
    }

    @Test
    public void saveTest() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName(ORDER_ITEM_NAME_2);
        orderItem2.setDescription(ORDER_ITEM_DESCRIPTION_2);

        Order order = new Order();
        order.setName(NAME_2);
        order.setDescription(DESCRIPTION_2);
        order.setTotalPrice(TOTAL_PRICE);
        order.setCustomerMail(MAIL);
        order.setOrderDate(DATE);
        order.setPaymentSign(SIGN);
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));
        order = orderService.save(order);
        long testOrderId = order.getId();

        Order loadedOrder = orderService.findById(testOrderId);

        Assert.assertEquals(testOrderId, loadedOrder.getId());
        Assert.assertEquals(NAME_2, loadedOrder.getName());
        Assert.assertEquals(DESCRIPTION_2, loadedOrder.getDescription());
        Assert.assertEquals(TOTAL_PRICE, loadedOrder.getTotalPrice(), 0);
        Assert.assertEquals(MAIL, loadedOrder.getCustomerMail());
        Assert.assertEquals(DATE, loadedOrder.getOrderDate());
        Assert.assertEquals(SIGN, loadedOrder.getPaymentSign());
        Assert.assertEquals(2, loadedOrder.getOrderItems().size());

        orderService.delete(loadedOrder.getId());
    }

    @Test
    public void findByIdTest() {
        Order loadedOrder = orderService.findById(orderId);

        Assert.assertEquals(orderId, loadedOrder.getId());
        Assert.assertEquals(NAME_1, loadedOrder.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOrder.getDescription());
        Assert.assertEquals(TOTAL_PRICE, loadedOrder.getTotalPrice(), 0);
        Assert.assertEquals(MAIL, loadedOrder.getCustomerMail());
        Assert.assertEquals(DATE, loadedOrder.getOrderDate());
        Assert.assertEquals(SIGN, loadedOrder.getPaymentSign());
        Assert.assertEquals(2, loadedOrder.getOrderItems().size());
    }

    @Test
    public void findByNameTest() {
        Order loadedOrder = orderService.findByName(NAME_1);

        Assert.assertEquals(orderId, loadedOrder.getId());
        Assert.assertEquals(NAME_1, loadedOrder.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOrder.getDescription());
        Assert.assertEquals(TOTAL_PRICE, loadedOrder.getTotalPrice(), 0);
        Assert.assertEquals(MAIL, loadedOrder.getCustomerMail());
        Assert.assertEquals(DATE, loadedOrder.getOrderDate());
        Assert.assertEquals(SIGN, loadedOrder.getPaymentSign());
        Assert.assertEquals(2, loadedOrder.getOrderItems().size());
    }

    @Test
    public void findAllTest() {
        Order order = new Order();
        order.setName(NAME_2);
        order.setDescription(DESCRIPTION_2);
        order.setTotalPrice(TOTAL_PRICE);
        order.setCustomerMail(MAIL);
        order.setOrderDate(DATE);
        order.setPaymentSign(SIGN);
        order = orderService.save(order);
        long testOrderId = order.getId();

        Assert.assertEquals(2, orderService.findAll().size());

        orderService.delete(testOrderId);
    }

    @Test
    public void updateTest() {
        Order order = orderService.findById(orderId);
        order.setName(NAME_3);
        order.setDescription(DESCRIPTION_3);
        order.setTotalPrice(UPDATED_TOTAL_PRICE);
        order = orderService.update(order);

        Order loadedOrder = orderService.findById(orderId);

        Assert.assertEquals(order.getId(), loadedOrder.getId());
        Assert.assertEquals(NAME_3, loadedOrder.getName());
        Assert.assertEquals(DESCRIPTION_3, loadedOrder.getDescription());
        Assert.assertEquals(UPDATED_TOTAL_PRICE, loadedOrder.getTotalPrice(), 0);
        Assert.assertEquals(MAIL, loadedOrder.getCustomerMail());
        Assert.assertEquals(DATE, loadedOrder.getOrderDate());
        Assert.assertEquals(SIGN, loadedOrder.getPaymentSign());
        Assert.assertEquals(2, loadedOrder.getOrderItems().size());
    }

    @Test
    public void deleteTest() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName(ORDER_ITEM_NAME_2);
        orderItem2.setDescription(ORDER_ITEM_DESCRIPTION_2);

        Order order = new Order();
        order.setName(NAME_2);
        order.setDescription(DESCRIPTION_2);
        order.setTotalPrice(TOTAL_PRICE);
        order.setCustomerMail(MAIL);
        order.setOrderDate(DATE);
        order.setPaymentSign(SIGN);
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        order = orderService.save(order);
        long testOrderId = order.getId();
        orderService.delete(testOrderId);

        Assert.assertNull(orderService.findById(testOrderId));
    }

    @Test
    public void addOrderItemTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(ORDER_ITEM_NAME_3);
        orderItem.setDescription(ORDER_ITEM_DESCRIPTION_3);

        Order order = orderService.addOrderItem(orderId, orderItem);

        Assert.assertEquals(3, order.getOrderItems().size());
        Assert.assertEquals(ORDER_ITEM_NAME_3, order.getOrderItems().get(2).getName());
        Assert.assertEquals(ORDER_ITEM_DESCRIPTION_3, order.getOrderItems().get(2).getDescription());
    }

    @Test
    public void removeOrderItemTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(ORDER_ITEM_NAME_3);
        orderItem.setDescription(ORDER_ITEM_DESCRIPTION_3);

        Order order = orderService.addOrderItem(orderId, orderItem);

        Assert.assertEquals(3, order.getOrderItems().size());

        order = orderService.removeOrderItem(orderId, order.getOrderItems().get(2));

        Assert.assertEquals(2, order.getOrderItems().size());
    }

    @Test
    public void findOrdersByPaymentStatusTest() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName(ORDER_ITEM_NAME_2);
        orderItem2.setDescription(ORDER_ITEM_DESCRIPTION_2);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setName(ORDER_ITEM_NAME_1);
        orderItem3.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem4 = new OrderItem();
        orderItem4.setName(ORDER_ITEM_NAME_2);
        orderItem4.setDescription(ORDER_ITEM_DESCRIPTION_2);

        Order order1 = new Order();
        order1.setName(NAME_2);
        order1.setDescription(DESCRIPTION_2);
        order1.setTotalPrice(TOTAL_PRICE);
        order1.setCustomerMail(MAIL);
        order1.setOrderDate(DATE);
        order1.setPaymentSign(SIGN);
        order1.setOrderItems(Arrays.asList(orderItem1, orderItem2));
        orderService.save(order1);

        Order order2 = new Order();
        order2.setName(NAME_3);
        order2.setDescription(DESCRIPTION_3);
        order2.setTotalPrice(TOTAL_PRICE);
        order2.setCustomerMail(MAIL);
        order2.setOrderDate(DATE);
        order2.setPaymentSign(SIGN);
        order2.setPaymentStatus(Payments.PAID.value());
        order2.setOrderItems(Arrays.asList(orderItem3, orderItem4));
        orderService.save(order2);

        List<Order> paidOrders = null;
        List<Order> unpaidOrders = null;

        try {
            paidOrders = orderService.findOrdersByPaymentStatus(Payments.PAID.value());
            unpaidOrders = orderService.findOrdersByPaymentStatus(Payments.UNPAID.value());
        } catch (PaymentStatusException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(1, paidOrders.size());
        Assert.assertEquals(2, unpaidOrders.size());
        Assert.assertEquals(NAME_3, paidOrders.get(0).getName());
        Assert.assertEquals(NAME_1, unpaidOrders.get(0).getName());
        Assert.assertEquals(NAME_2, unpaidOrders.get(1).getName());

        orderService.delete(order1.getId());
        orderService.delete(order2.getId());
    }

    @Test
    public void payForOrderTest() {
        Order order = orderService.findById(orderId);
        try {
            order = orderService.payForOrder(orderId);
        } catch (PayForOrderException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(Payments.PAID.value(), order.getPaymentStatus());
    }

}

import com.netcracker.parfenenko.InventoryApplication;
import com.netcracker.parfenenko.entity.Order;
import com.netcracker.parfenenko.entity.OrderItem;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.service.OrderService;
import com.netcracker.parfenenko.util.Statuses;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplication.class)
public class OrderTest {

    @Autowired
    private OrderService orderService;

    private long orderId;
    private String orderName;

    private final String DESCRIPTION_1 = "Test description 1";
    private final String DESCRIPTION_2 = "Test description 2";
    private final String DESCRIPTION_3 = "Updated test description 1";
    private final String MAIL_1 = "test@test.com";
    private final String MAIL_2 = "test1@test.com";

    private final String ORDER_ITEM_NAME_1 = "Test OrderItem 1";
    private final String ORDER_ITEM_NAME_2 = "Test OrderItem 2";
    private final String ORDER_ITEM_DESCRIPTION_1 = "Test OrderItem description 1";
    private final String ORDER_ITEM_DESCRIPTION_2 = "Test OrderItem description 2";
    private final String CATEGORY = "Test Category";
    private final double PRICE = 3.99;

    @Before
    public void initOrder() {
        Order order = new Order();
        order.setDescription(DESCRIPTION_1);
        order.setCustomerMail(MAIL_1);

        order = orderService.save(order);
        orderId = order.getId();
        orderName = order.getName();
    }

    @After
    public void destroyOrder() {
        orderService.delete(orderId);
    }

    @Test
    public void saveTest() {
        Order order = new Order();
        order.setDescription(DESCRIPTION_2);
        order.setCustomerMail(MAIL_1);
        order = orderService.save(order);
        long testOrderId = order.getId();

        Order loadedOrder = orderService.findById(testOrderId);

        Assert.assertEquals(testOrderId, loadedOrder.getId());
        Assert.assertEquals(DESCRIPTION_2, loadedOrder.getDescription());
        Assert.assertEquals(MAIL_1, loadedOrder.getCustomerMail());

        orderService.delete(loadedOrder.getId());
    }

    @Test
    public void findByIdTest() {
        Order loadedOrder = orderService.findById(orderId);

        Assert.assertEquals(orderId, loadedOrder.getId());
        Assert.assertEquals(DESCRIPTION_1, loadedOrder.getDescription());
        Assert.assertEquals(MAIL_1, loadedOrder.getCustomerMail());
    }

    @Test
    public void findByNameTest() {
        Order loadedOrder = orderService.findByName(orderName);

        Assert.assertEquals(orderId, loadedOrder.getId());
        Assert.assertEquals(DESCRIPTION_1, loadedOrder.getDescription());
        Assert.assertEquals(MAIL_1, loadedOrder.getCustomerMail());
    }

    @Test
    public void findAllTest() {
        int currentSize = orderService.findAll().size();

        Order order = new Order();
        order.setDescription(DESCRIPTION_2);
        order.setCustomerMail(MAIL_1);
        order = orderService.save(order);
        long testOrderId = order.getId();

        Assert.assertEquals(currentSize + 1, orderService.findAll().size());

        orderService.delete(testOrderId);
    }

    @Test
    public void updateTest() {
        Order order = orderService.findById(orderId);
        order.setDescription(DESCRIPTION_2);
        order.setCustomerMail(MAIL_2);
        order = orderService.update(order);

        Order loadedOrder = orderService.findById(orderId);

        Assert.assertEquals(order.getId(), loadedOrder.getId());
        Assert.assertEquals(DESCRIPTION_2, loadedOrder.getDescription());
        Assert.assertEquals(MAIL_2, loadedOrder.getCustomerMail());
        Assert.assertEquals(orderName, loadedOrder.getName());
    }

    @Test
    public void deleteTest() {
        Order order = new Order();
        order.setDescription(DESCRIPTION_2);
        order.setCustomerMail(MAIL_1);

        order = orderService.save(order);
        long testOrderId = order.getId();
        orderService.delete(testOrderId);

        try {
            order = orderService.findById(testOrderId);
        } catch (NoContentException e) {
            order = null;
        }
        Assert.assertNull(order);
    }

    @Test
    public void findOrderItemsTest() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);
        orderItem1.setCategory(CATEGORY);
        orderItem1.setPrice(PRICE);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName(ORDER_ITEM_NAME_2);
        orderItem2.setDescription(ORDER_ITEM_DESCRIPTION_2);
        orderItem2.setCategory(CATEGORY);
        orderItem2.setPrice(PRICE);

        orderService.addOrderItem(orderId, orderItem1);
        orderService.addOrderItem(orderId, orderItem2);

        Assert.assertEquals(2, orderService.findOrderItems(orderId).size());
    }

    @Test
    public void addOrderItemTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(ORDER_ITEM_NAME_1);
        orderItem.setDescription(ORDER_ITEM_DESCRIPTION_1);
        orderItem.setCategory(CATEGORY);
        orderItem.setPrice(PRICE);

        Order order = orderService.addOrderItem(orderId, orderItem);

        Assert.assertEquals(1, order.getOrderItems().size());
    }

    @Test
    public void removeOrderItemTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(ORDER_ITEM_NAME_1);
        orderItem.setDescription(ORDER_ITEM_DESCRIPTION_1);
        orderItem.setCategory(CATEGORY);
        orderItem.setPrice(PRICE);

        Order order = orderService.addOrderItem(orderId, orderItem);

        Assert.assertEquals(1, order.getOrderItems().size());

        order = orderService.removeOrderItem(orderId, order.getOrderItems().iterator().next().getId());

        Assert.assertEquals(0, order.getOrderItems().size());
    }

    @Test
    public void findOrdersByPaymentStatusTest() {
        int currentPaidOrders = orderService.findOrdersByPaymentStatus(Statuses.PAID.value()).size();
        int currentOpenedOrders = orderService.findOrdersByPaymentStatus(Statuses.OPENED.value()).size();

        Order order1 = new Order();
        order1.setDescription(DESCRIPTION_2);
        order1.setCustomerMail(MAIL_1);
        order1 = orderService.save(order1);

        Order order2 = new Order();
        order2.setDescription(DESCRIPTION_3);
        order2.setCustomerMail(MAIL_1);
        order2 = orderService.save(order2);

        orderService.updateStatus(order2.getId(), Statuses.PAID.value());

        List<Order> paidOrders = orderService.findOrdersByPaymentStatus(Statuses.PAID.value());
        List<Order> unpaidOrders = orderService.findOrdersByPaymentStatus(Statuses.OPENED.value());

        Assert.assertEquals(currentPaidOrders + 1, paidOrders.size());
        Assert.assertEquals(currentOpenedOrders + 1, unpaidOrders.size());

        orderService.delete(order1.getId());
        orderService.delete(order2.getId());
    }

    @Test
    public void updateStatusTest() {
        Order order = new Order();
        order.setDescription(DESCRIPTION_2);
        order.setCustomerMail(MAIL_1);
        order = orderService.save(order);
        order = orderService.updateStatus(order.getId(), Statuses.PAID.value());

        Assert.assertEquals(Statuses.PAID.value(), order.getPaymentStatus());

        orderService.delete(order.getId());
    }

}

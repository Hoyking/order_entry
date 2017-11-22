import com.netcracker.parfenenko.Application;
import com.netcracker.parfenenko.entities.InventoryOrder;
import com.netcracker.parfenenko.entities.OrderItem;
import com.netcracker.parfenenko.service.OrderService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InventoryOrderTest {

    @Autowired
    private OrderService orderService;

    private long orderId;
    private final String NAME_1 = "Test InventoryOrder 1";
    private final String NAME_2 = "Test InventoryOrder 2";
    private final String UPDATED_NAME = "Updated test InventoryOrder 1";
    private final String DESCRIPTION_1 = "Test description 1";
    private final String DESCRIPTION_2 = "Test description 2";
    private final String UPDATED_DESCRIPTION = "Updated test description 1";
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

        InventoryOrder order = new InventoryOrder();
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

        InventoryOrder order = new InventoryOrder();
        order.setName(NAME_2);
        order.setDescription(DESCRIPTION_2);
        order.setTotalPrice(TOTAL_PRICE);
        order.setCustomerMail(MAIL);
        order.setOrderDate(DATE);
        order.setPaymentSign(SIGN);
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));
        order = orderService.save(order);
        long testOrderId = order.getId();

        InventoryOrder loadedOrder = orderService.findById(testOrderId);

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
        InventoryOrder loadedOrder = orderService.findById(orderId);

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
        InventoryOrder loadedOrder = orderService.findByName(NAME_1);

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
        InventoryOrder order = new InventoryOrder();
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
        InventoryOrder order = orderService.findById(orderId);
        order.setName(UPDATED_NAME);
        order.setDescription(UPDATED_DESCRIPTION);
        order.setTotalPrice(UPDATED_TOTAL_PRICE);
        order = orderService.update(order);

        InventoryOrder loadedOrder = orderService.findById(orderId);

        Assert.assertEquals(order.getId(), loadedOrder.getId());
        Assert.assertEquals(UPDATED_NAME, loadedOrder.getName());
        Assert.assertEquals(UPDATED_DESCRIPTION, loadedOrder.getDescription());
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

        InventoryOrder order = new InventoryOrder();
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

        InventoryOrder order = orderService.addOrderItem(orderId, orderItem);

        Assert.assertEquals(3, order.getOrderItems().size());
        Assert.assertEquals(ORDER_ITEM_NAME_3, order.getOrderItems().get(2).getName());
        Assert.assertEquals(ORDER_ITEM_DESCRIPTION_3, order.getOrderItems().get(2).getDescription());
    }

    @Test
    public void removeOrderItemTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setName(ORDER_ITEM_NAME_3);
        orderItem.setDescription(ORDER_ITEM_DESCRIPTION_3);

        InventoryOrder order = orderService.addOrderItem(orderId, orderItem);

        Assert.assertEquals(3, order.getOrderItems().size());

        order = orderService.removeOrderItem(orderId, order.getOrderItems().get(2));

        Assert.assertEquals(2, order.getOrderItems().size());
    }

}

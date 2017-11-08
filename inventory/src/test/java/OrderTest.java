import com.netcracker.parfenenko.dao.OrderDAO;
import com.netcracker.parfenenko.entities.Order;
import com.netcracker.parfenenko.entities.OrderItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class OrderTest {

    private long orderId;
    private final String NAME_1 = "Test Order 1";
    private final String NAME_2 = "Test Order 2";
    private final String UPDATED_NAME = "Updated test Order 1";
    private final String DESCRIPTION_1 = "Test description 1";
    private final String DESCRIPTION_2 = "Test description 2";
    private final String UPDATED_DESCRIPTION = "Updated test description 1";
    private final double TOTAL_PRICE = 0.99;
    private final double UPDATED_TOTAL_PRICE = 1.99;
    private final String MAIL = "Test mail";
    private final String DATE = "Test date";
    private final String SIGN = "Test sign";

    private final long ID1 = 998;
    private final long ID2 = 999;
    private final String ORDER_ITEM_NAME_1 = "Test OrderItem 1";
    private final String ORDER_ITEM_NAME_2 = "Test OrderItem 2";
    private final String ORDER_ITEM_DESCRIPTION_1 = "Test OrderItem description 1";
    private final String ORDER_ITEM_DESCRIPTION_2 = "Test OrderItem description 2";

    private OrderDAO orderDAO;

    @Before
    public void initOrder() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(ID1);
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(ID2);
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

        orderId = orderDAO.save(order);
    }

    @After
    public void destroyOrder() {
        orderDAO.delete(orderId);
    }

    @Test
    public void saveTest() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(ID1);
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(ID2);
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
        long testOrderId = orderDAO.save(order);
        order.setId(testOrderId);

        Order loadedOrder = orderDAO.findById(testOrderId);

        Assert.assertEquals(testOrderId, loadedOrder.getId());
        Assert.assertEquals(NAME_2, loadedOrder.getName());
        Assert.assertEquals(DESCRIPTION_2, loadedOrder.getDescription());
        Assert.assertEquals(TOTAL_PRICE, loadedOrder.getTotalPrice(), 0);
        Assert.assertEquals(MAIL, loadedOrder.getCustomerMail());
        Assert.assertEquals(DATE, loadedOrder.getOrderDate());
        Assert.assertEquals(SIGN, loadedOrder.getPaymentSign());
        Assert.assertEquals(2, loadedOrder.getOrderItems().size());

        orderDAO.delete(loadedOrder.getId());
    }

    @Test
    public void findByIdTest() {
        Order loadedOrder = orderDAO.findById(orderId);

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
        Order loadedOrder = orderDAO.findByName(NAME_1);

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
        long testOrderId = orderDAO.save(order);

        Assert.assertEquals(2, orderDAO.findAll().size());

        orderDAO.delete(testOrderId);
    }

    @Test
    public void updateTest() {
        Order order = orderDAO.findById(orderId);
        order.setName(UPDATED_NAME);
        order.setDescription(UPDATED_DESCRIPTION);
        order.setTotalPrice(UPDATED_TOTAL_PRICE);
        orderDAO.update(order);

        Order loadedOrder = orderDAO.findById(orderId);

        Assert.assertEquals(orderId, loadedOrder.getId());
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
        orderItem1.setId(ID1);
        orderItem1.setName(ORDER_ITEM_NAME_1);
        orderItem1.setDescription(ORDER_ITEM_DESCRIPTION_1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(ID2);
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

        long testOrderId = orderDAO.save(order);
        orderDAO.delete(testOrderId);

        Assert.assertNull(orderDAO.findById(testOrderId));
    }

}

import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entities.Price;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PriceTest {

    private long priceId;
    private final double PRICE_1 = 0.99;
    private final double PRICE_2 = 1.99;
    private final double UPDATED_PRICE = 1.49;

    private static PriceDAO priceDAO;

    @Before
    public void initPrice() {
        Price price = new Price();
        price.setValue(PRICE_1);

        priceId = priceDAO.save(price);
    }

    @After
    public void destroyPrice() {
        priceDAO.delete(priceId);
    }

    @Test
    public void saveTest() {
        Price price = new Price();
        price.setValue(PRICE_2);

        long testPriceId = priceDAO.save(price);
        price.setId(testPriceId);

        Price loadedPrice = priceDAO.findById(testPriceId);

        Assert.assertEquals(testPriceId, loadedPrice.getId());
        Assert.assertEquals(PRICE_2, loadedPrice.getValue(), 0);

        priceDAO.delete(loadedPrice.getId());
    }

    @Test
    public void findByIdTest() {
        Price loadedPrice = priceDAO.findById(priceId);

        Assert.assertEquals(priceId, loadedPrice.getId());
        Assert.assertEquals(PRICE_1, loadedPrice.getValue(), 0);
    }

    @Test
    public void findAllTest() {
        Price price = new Price();
        price.setValue(PRICE_2);

        long testPriceId = priceDAO.save(price);

        Assert.assertEquals(2, priceDAO.findAll().size());

        priceDAO.delete(testPriceId);
    }

    @Test
    public void updateTest() {
        Price price = new Price();
        price.setValue(UPDATED_PRICE);
        price.setId(priceId);

        priceDAO.update(price);
        Price loadedPrice = priceDAO.findById(priceId);

        Assert.assertEquals(priceId, loadedPrice.getId());
        Assert.assertEquals(UPDATED_PRICE, loadedPrice.getValue(), 0);
    }

    @Test
    public void deleteTest() {
        Price price = new Price();
        price.setValue(PRICE_2);

        long testPriceId = priceDAO.save(price);
        priceDAO.delete(testPriceId);

        Assert.assertNull(priceDAO.findById(testPriceId));
    }

}

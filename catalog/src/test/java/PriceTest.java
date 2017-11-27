import com.netcracker.parfenenko.Application;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.service.PriceService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PriceTest {

    @Autowired
    private PriceService priceService;

    private long priceId;
    private final double PRICE_1 = 0.99;
    private final double PRICE_2 = 1.99;
    private final double UPDATED_PRICE = 1.49;

    @Before
    public void initPrice() {
        Price price = new Price();
        price.setValue(PRICE_1);

        price = priceService.save(price);
        priceId = price.getId();
    }

    @After
    public void destroyPrice() {
        priceService.delete(priceId);
    }

    @Test
    public void saveTest() {
        Price price = new Price();
        price.setValue(PRICE_2);

        price = priceService.save(price);
        long testPriceId = price.getId();

        Price loadedPrice = priceService.findById(testPriceId);

        Assert.assertEquals(testPriceId, loadedPrice.getId());
        Assert.assertEquals(PRICE_2, loadedPrice.getValue(), 0);

        priceService.delete(loadedPrice.getId());
    }

    @Test
    public void findByIdTest() {
        Price loadedPrice = priceService.findById(priceId);

        Assert.assertEquals(priceId, loadedPrice.getId());
        Assert.assertEquals(PRICE_1, loadedPrice.getValue(), 0);
    }

    @Test
    public void findAllTest() {
        Price price = new Price();
        price.setValue(PRICE_2);

        price = priceService.save(price);
        long testPriceId = price.getId();

        Assert.assertEquals(2, priceService.findAll().size());

        priceService.delete(testPriceId);
    }

    @Test
    public void updateTest() {
        Price price = new Price();
        price.setValue(UPDATED_PRICE);
        price.setId(priceId);

        price = priceService.update(price);
        Price loadedPrice = priceService.findById(priceId);

        Assert.assertEquals(price.getId(), loadedPrice.getId());
        Assert.assertEquals(UPDATED_PRICE, loadedPrice.getValue(), 0);
    }

    @Test
    public void deleteTest() {
        Price price = new Price();
        price.setValue(PRICE_2);

        price = priceService.save(price);
        long testPriceId = price.getId();
        priceService.delete(testPriceId);

        Assert.assertNull(priceService.findById(testPriceId));
    }

}

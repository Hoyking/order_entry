import com.netcracker.parfenenko.dao.*;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import org.junit.*;

import java.util.Arrays;

public class OfferTest {

    private long offerId;
    private final String OFFER_NAME_1 = "Test offer 1";
    private final String UPDATED_OFFER_NAME = "Updated test offer 1";
    private final String OFFER_NAME_2 = "Test offer 2";
    private final String DESCRIPTION_1 = "Description 1";
    private final String UPDATED_DESCRIPTION = "Updated description 1";
    private final String DESCRIPTION_2 = "Description 1";
    private long categoryId;
    private final String CATEGORY_NAME = "Test category 1";
    private long tagId1;
    private final String TAG_NAME_1 = "Test tag 1";
    private long tagId2;
    private final String TAG_NAME_2 = "Test tag 2";
    private long priceId;
    private final double PRICE_VALUE = 2.99;

    private static OfferDAO offerDAO;
    private static CategoryDAO categoryDAO;
    private static PriceDAO priceDAO;
    private static TagDAO tagDAO;

    @BeforeClass
    public static void init() {
        offerDAO = JPAOfferDAO.getInstance();
        categoryDAO = JPACategoryDAO.getInstance();
        priceDAO = JPAPriceDAO.getInstance();
        tagDAO = JPATagDAO.getInstance();
    }

    @Before
    public void initOffer() {
        Price price = new Price();
        price.setValue(PRICE_VALUE);
        priceDAO.save(price);
        priceId = price.getId();
        price.setId(priceId);

        Category category = new Category();
        category.setName(CATEGORY_NAME);
        categoryDAO.save(category);
        categoryId = category.getId();
        category.setId(categoryId);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);
        tagDAO.save(tag1);
        tagId1 = tag1.getId();
        tag1.setId(tagId1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);
        tagDAO.save(tag2);
        tagId2 = tag2.getId();
        tag2.setId(tagId2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_1);
        offer.setDescription(DESCRIPTION_1);
        offer.setName(OFFER_NAME_1);
        offer.setDescription(DESCRIPTION_1);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(Arrays.asList(tag1, tag2));

        offerDAO.save(offer);
        offerId = offer.getId();
    }

    @After
    public void destroyOffer() {
        offerDAO.delete(offerId);
        categoryDAO.delete(categoryId);
        priceDAO.delete(priceId);
        tagDAO.delete(tagId1);
        tagDAO.delete(tagId2);
    }

    @Test
    public void saveTest() {
        Category category = categoryDAO.findById(categoryId);
        Price price = priceDAO.findById(priceId);
        Tag tag1 = tagDAO.findById(tagId1);
        Tag tag2 = tagDAO.findById(tagId2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(Arrays.asList(tag1, tag2));

        offerDAO.save(offer);
        long testOfferId = offer.getId();

        Offer loadedOffer = offerDAO.findById(testOfferId);

        Assert.assertEquals(testOfferId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_2, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_2, loadedOffer.getDescription());
        Assert.assertEquals(category.getName(), loadedOffer.getCategory().getName());
        Assert.assertEquals(price.getValue(), loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, loadedOffer.getTags().size());

        offerDAO.delete(loadedOffer.getId());
    }

    @Test
    public void findByIdTest() {
        Offer loadedOffer = offerDAO.findById(offerId);

        Assert.assertEquals(offerId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_1, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, loadedOffer.getTags().size());
    }

    @Test
    public void findByNameTest() {
        Offer loadedOffer = offerDAO.findByName(OFFER_NAME_1);

        Assert.assertEquals(offerId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_1, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, loadedOffer.getTags().size());
    }

    @Test
    public void findAllTest() {
        Offer offer = new Offer();
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offerDAO.save(offer);
        long testOfferId = offer.getId();

        Assert.assertEquals(2, offerDAO.findAll().size());

        offerDAO.delete(testOfferId);
    }

    @Test
    public void updateTest() {
        Offer offer = offerDAO.findById(offerId);
        offer.setName(UPDATED_OFFER_NAME);
        offer.setDescription(UPDATED_DESCRIPTION);
        offerDAO.update(offer);

        Offer loadedOffer = offerDAO.findById(offerId);

        Assert.assertEquals(offer.getId(), loadedOffer.getId());
        Assert.assertEquals(UPDATED_OFFER_NAME, loadedOffer.getName());
        Assert.assertEquals(UPDATED_DESCRIPTION, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, loadedOffer.getTags().size());
    }

    @Test
    public void deleteTest() {
        Category category = categoryDAO.findById(categoryId);
        Price price = priceDAO.findById(priceId);
        Tag tag1 = tagDAO.findById(tagId1);
        Tag tag2 = tagDAO.findById(tagId2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(Arrays.asList(tag1, tag2));

        offerDAO.save(offer);
        long testOfferId = offer.getId();
        offerDAO.delete(testOfferId);

        Assert.assertNull(offerDAO.findById(testOfferId));
    }

}

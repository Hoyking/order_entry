import com.netcracker.parfenenko.CatalogApplication;
import com.netcracker.parfenenko.entity.Category;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.entity.Price;
import com.netcracker.parfenenko.entity.Tag;
import com.netcracker.parfenenko.exception.NoContentException;
import com.netcracker.parfenenko.service.CategoryService;
import com.netcracker.parfenenko.service.OfferService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogApplication.class)
public class OfferTest {

    @Autowired
    private OfferService offerService;
    @Autowired
    private CategoryService categoryService;

    private long offerId;
    private final String OFFER_NAME_1 = "Test offer 1";
    private final String OFFER_NAME_2 = "Test offer 2";
    private final String OFFER_NAME_3 = "Test offer 3";
    private final String DESCRIPTION_1 = "Description 1";
    private final String DESCRIPTION_2 = "Description 2";
    private final String DESCRIPTION_3 = "Description 3";
    private long categoryId;
    private final String CATEGORY_NAME_1 = "Test category 1";
    private final String TAG_NAME_1 = "Test tag 1";
    private final String TAG_NAME_2 = "Test tag 2";
    private final String TAG_NAME_3 = "Test tag 3";
    private final double PRICE_VALUE_1 = 2.99;
    private final double PRICE_VALUE_2 = 3.99;
    private final double PRICE_VALUE_3 = 4.99;

    @Before
    public void initOffer() {
        Price price = new Price();
        price.setValue(PRICE_VALUE_1);

        Category category = new Category();
        category.setName(CATEGORY_NAME_1);
        category = categoryService.save(category);
        categoryId = category.getId();
        category.setId(categoryId);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_1);
        offer.setDescription(DESCRIPTION_1);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        offer = offerService.save(offer);
        offerId = offer.getId();
    }

    @After
    public void destroyOffer() {
        offerService.delete(offerId);
        categoryService.delete(categoryId);
    }

    @Test
    public void saveTest() {
        Category category = categoryService.findById(categoryId);

        Price price = new Price();
        price.setValue(PRICE_VALUE_1);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_3);
        offer.setDescription(DESCRIPTION_3);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        offer = offerService.save(offer);
        long testOfferId = offer.getId();

        Offer loadedOffer = offerService.findById(testOfferId);

        Assert.assertEquals(testOfferId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_3, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_3, loadedOffer.getDescription());
        Assert.assertEquals(category.getName(), loadedOffer.getCategory().getName());
        Assert.assertEquals(price.getValue(), loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, offerService.findTags(loadedOffer.getId()).size());

        offerService.delete(loadedOffer.getId());
    }

    @Test
    public void findByIdTest() {
        Offer loadedOffer = offerService.findById(offerId);

        Assert.assertEquals(offerId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_1, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME_1, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE_1, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, offerService.findTags(loadedOffer.getId()).size());
    }

    @Test
    public void findByNameTest() {
        Offer loadedOffer = offerService.findByName(OFFER_NAME_1);

        Assert.assertEquals(offerId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_1, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME_1, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE_1, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, offerService.findTags(loadedOffer.getId()).size());
    }

    @Test
    public void findByPartOfNameTest() {
        int currentNum = offerService.findByPartOfName("Test offer").size();

        Category category = categoryService.findById(categoryId);

        Price price = new Price();
        price.setValue(PRICE_VALUE_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_3);
        offer.setDescription(DESCRIPTION_3);
        offer.setCategory(category);
        offer.setPrice(price);
        offer = offerService.save(offer);
        long testOfferId = offer.getId();

        Assert.assertEquals(currentNum + 1, offerService.findByPartOfName("Test offer").size());

        offerService.delete(testOfferId);
    }

    @Test
    public void findAllTest() {
        int currentSize = offerService.findAll().size();

        Category category = categoryService.findById(categoryId);

        Price price = new Price();
        price.setValue(PRICE_VALUE_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_3);
        offer.setDescription(DESCRIPTION_3);
        offer.setCategory(category);
        offer.setPrice(price);
        offer = offerService.save(offer);
        long testOfferId = offer.getId();

        Assert.assertEquals(currentSize + 1, offerService.findAll().size());

        offerService.delete(testOfferId);
    }

    @Test
    public void updateTest() {
        Offer offer = offerService.findById(offerId);
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offer = offerService.update(offer);

        Offer loadedOffer = offerService.findById(offerId);

        Assert.assertEquals(offer.getId(), loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_2, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_2, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME_1, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE_1, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, offerService.findTags(loadedOffer.getId()).size());
    }

    @Test
    public void deleteTest() {
        Category category = categoryService.findById(categoryId);

        Price price = new Price();
        price.setValue(PRICE_VALUE_1);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_3);
        offer.setDescription(DESCRIPTION_3);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        offer = offerService.save(offer);
        long testOfferId = offer.getId();
        offerService.delete(testOfferId);

        try {
            offer = offerService.findById(testOfferId);
        } catch (NoContentException e) {
            offer = null;
        }

        Assert.assertNull(offer);
    }

    @Test
    public void findByFilterTest() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("categories", Collections.singletonList(categoryId + ""));
        filters.put("tags", Collections.singletonList(TAG_NAME_1));
        filters.put("price", Arrays.asList((PRICE_VALUE_1 - 1) + "", (PRICE_VALUE_1 + 1) + ""));

        List<Offer> offers = offerService.findByFilter(filters);

        Assert.assertEquals(1, offers.size());
        Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
    }

    @Test
    public void changeAvailabilityTest() {
        Offer offer = offerService.changeAvailability(offerId);
        Assert.assertEquals(false, offer.isAvailable());
    }

    @Test
    public void findOffersByTagsTest() {
        Category category = categoryService.findById(categoryId);

        Price price = new Price();
        price.setValue(PRICE_VALUE_1);

        Price price1 = new Price();
        price1.setValue(PRICE_VALUE_2);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Tag tag3 = new Tag();
        tag3.setName(TAG_NAME_3);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offer.setCategory(category);
        offer.setPrice(price);
        offer.setTags(new HashSet<>(Arrays.asList(tag1, tag2, tag3)));
        offer = offerService.save(offer);

        Offer offer1 = new Offer();
        offer1.setName(OFFER_NAME_3);
        offer1.setDescription(DESCRIPTION_3);
        offer1.setCategory(category);
        offer1.setPrice(price1);
        offer1.setTags(new HashSet<>(Arrays.asList(tag1, tag3)));
        offer1 = offerService.save(offer1);

        List<Offer> loadedOffers1 = offerService.findByTags(Arrays.asList(tag1.getName(), tag2.getName()));
        List<Offer> loadedOffers2 = offerService.findByTags(Arrays.asList(tag1.getName(), tag3.getName()));

        Assert.assertEquals(3, loadedOffers1.size());
        Assert.assertEquals(3, loadedOffers2.size());

        offerService.delete(offer.getId());
        offerService.delete(offer1.getId());
    }

    @Test
    public void findAvailableOffersTest() {
        int currentSize = offerService.findAvailableOffers().size();

        Category category = categoryService.findById(categoryId);

        Price price = new Price();
        price.setValue(PRICE_VALUE_1);

        Price price1 = new Price();
        price1.setValue(PRICE_VALUE_2);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Tag tag3 = new Tag();
        tag3.setName(TAG_NAME_3);

        Offer offer2 = new Offer();
        offer2.setName(OFFER_NAME_2);
        offer2.setDescription(DESCRIPTION_2);
        offer2.setTags(new HashSet<>(Arrays.asList(tag1, tag2, tag3)));
        offer2.setCategory(category);
        offer2.setPrice(price);
        offer2 = offerService.save(offer2);

        List<Offer> offers = offerService.findAvailableOffers();

        Assert.assertEquals(currentSize + 1, offers.size());

        offerService.delete(offer2.getId());
    }

    @Test
    public void updatePriceTest() {
        Offer offer = offerService.updatePrice(offerId, PRICE_VALUE_2);
        Assert.assertEquals(PRICE_VALUE_2, offer.getPrice().getValue(), 0);
    }

    @Test
    public void findOffersOfIntervalTest() {
        int currentSize = offerService.findOffersOfPriceInterval(2, 4).size();

        Category category = categoryService.findById(categoryId);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Tag tag3 = new Tag();
        tag3.setName(TAG_NAME_3);

        Price price1 = new Price();
        price1.setValue(PRICE_VALUE_2);
        Price price2 = new Price();
        price2.setValue(PRICE_VALUE_3);

        Offer offer2 = new Offer();
        offer2.setName(OFFER_NAME_2);
        offer2.setDescription(DESCRIPTION_2);
        offer2.setTags(new HashSet<>(Arrays.asList(tag1, tag2, tag3)));
        offer2.setCategory(category);
        offer2.setPrice(price1);
        offer2 = offerService.save(offer2);

        Offer offer3 = new Offer();
        offer3.setName(OFFER_NAME_3);
        offer3.setDescription(DESCRIPTION_3);
        offer3.setTags(new HashSet<>(Arrays.asList(tag1, tag3)));
        offer3.setCategory(category);
        offer3.setPrice(price2);
        offer3 = offerService.save(offer3);

        List<Offer> offers = offerService.findOffersOfPriceInterval(2, 4);

        Assert.assertEquals(currentSize + 1, offers.size());

        offerService.delete(offer2.getId());
        offerService.delete(offer3.getId());
    }

    @Test
    public void addTagToOfferTest() {
        int currentNum = offerService.findTags(offerId).size();
        offerService.addTagToOffer(offerId, TAG_NAME_3);
        Assert.assertEquals(currentNum + 1, offerService.findTags(offerId).size());
    }

    @Test
    public void removeTagFromOfferTest() {
        int currentNum = offerService.findTags(offerId).size();
        offerService.addTagToOffer(offerId, TAG_NAME_3);
        Assert.assertEquals(currentNum + 1, offerService.findTags(offerId).size());
        offerService.removeTagFromOffer(offerId, TAG_NAME_3);
        Assert.assertEquals(currentNum, offerService.findTags(offerId).size());
    }

    @Test
    public void findTagsTest() {
        Set<Tag> tags = offerService.findTags(offerId);
        Assert.assertEquals(2, tags.size());
    }

}

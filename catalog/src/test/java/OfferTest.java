import com.netcracker.parfenenko.Application;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
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
        Assert.assertEquals(2, loadedOffer.getTags().size());

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
        Assert.assertEquals(2, loadedOffer.getTags().size());
    }

    @Test
    public void findByNameTest() {
        Offer loadedOffer = offerService.findByName(OFFER_NAME_1);

        Assert.assertEquals(offerId, loadedOffer.getId());
        Assert.assertEquals(OFFER_NAME_1, loadedOffer.getName());
        Assert.assertEquals(DESCRIPTION_1, loadedOffer.getDescription());
        Assert.assertEquals(CATEGORY_NAME_1, loadedOffer.getCategory().getName());
        Assert.assertEquals(PRICE_VALUE_1, loadedOffer.getPrice().getValue(), 0);
        Assert.assertEquals(2, loadedOffer.getTags().size());
    }

    @Test
    public void findAllTest() {
        Offer offer = new Offer();
        offer.setName(OFFER_NAME_3);
        offer.setDescription(DESCRIPTION_3);
        offer = offerService.save(offer);
        long testOfferId = offer.getId();

        Assert.assertEquals(2, offerService.findAll().size());

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
        Assert.assertEquals(2, loadedOffer.getTags().size());
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

        Assert.assertNull(offerService.findById(testOfferId));
    }

    @Test
    public void changeAvailabilityTest() {
        Offer offer = offerService.changeAvailability(offerId);
        Assert.assertEquals(false, offer.isAvailable());
    }

    @Test
    public void findOffersByTagsTest() {
        Category category = categoryService.findById(categoryId);

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
        offer2 = offerService.save(offer2);

        Offer offer3 = new Offer();
        offer3.setName(OFFER_NAME_3);
        offer3.setDescription(DESCRIPTION_3);
        offer3.setTags(new HashSet<>(Arrays.asList(tag1, tag3)));
        offer3.setCategory(category);
        offer3 = offerService.save(offer3);

        List<Offer> loadedOffers1 = offerService.findOffersByTags(Arrays.asList(tag1.getName(), tag2.getName()));
        List<Offer> loadedOffers2 = offerService.findOffersByTags(Arrays.asList(tag1.getName(), tag3.getName()));

        Assert.assertEquals(2, loadedOffers1.size());
        Assert.assertEquals(OFFER_NAME_1, loadedOffers1.get(0).getName());
        Assert.assertEquals(OFFER_NAME_2, loadedOffers1.get(1).getName());

        Assert.assertEquals(2, loadedOffers2.size());
        Assert.assertEquals(OFFER_NAME_2, loadedOffers2.get(0).getName());
        Assert.assertEquals(OFFER_NAME_3, loadedOffers2.get(1).getName());

        offerService.delete(offer2.getId());
        offerService.delete(offer3.getId());
    }

    @Test
    public void findAvailableOffersTest() {
        Category category = categoryService.findById(categoryId);

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
        offer2 = offerService.save(offer2);

        Offer offer3 = new Offer();
        offer3.setName(OFFER_NAME_3);
        offer3.setDescription(DESCRIPTION_3);
        offer3.setTags(new HashSet<>(Arrays.asList(tag1, tag3)));
        offer3.setCategory(category);
        offer3.setAvailable(false);
        offer3 = offerService.save(offer3);

        List<Offer> offers = offerService.findAvailableOffers();

        Assert.assertEquals(2, offers.size());
        Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
        Assert.assertEquals(OFFER_NAME_2, offers.get(1).getName());

        offerService.delete(offer2.getId());
        offerService.delete(offer3.getId());
    }

    @Test
    public void addPriceToOfferTest() {
        Offer offer = offerService.findById(offerId);
        Price price = new Price();
        price.setValue(PRICE_VALUE_2);

        offer = offerService.addPriceToOffer(offer.getId(), price);

        Assert.assertEquals(PRICE_VALUE_2, offer.getPrice().getValue(), 0);
    }

    @Test
    public void findOffersOfIntervalTest() {
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

        Assert.assertEquals(2, offers.size());
        Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
        Assert.assertEquals(OFFER_NAME_2, offers.get(1).getName());

        offerService.delete(offer2.getId());
        offerService.delete(offer3.getId());
    }

    @Test
    public void addTagToOfferTest() {
        Category category = categoryService.findById(categoryId);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offer.setCategory(category);
        offer.setTags(new HashSet<>(Collections.singletonList(tag1)));
        offer = offerService.save(offer);

        Offer loadedOffer = offerService.findById(offer.getId());

        Assert.assertEquals(1, loadedOffer.getTags().size());

        loadedOffer = offerService.addTagToOffer(loadedOffer.getId(), tag2);

        Assert.assertEquals(2, loadedOffer.getTags().size());

        offerService.delete(loadedOffer.getId());
    }

    @Test
    public void removeTagFromOfferTest() {
        Category category = categoryService.findById(categoryId);

        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Offer offer = new Offer();
        offer.setName(OFFER_NAME_2);
        offer.setDescription(DESCRIPTION_2);
        offer.setCategory(category);
        offer.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));
        offer = offerService.save(offer);

        Offer loadedOffer = offerService.findById(offer.getId());

        Assert.assertEquals(2, loadedOffer.getTags().size());

        loadedOffer = offerService.removeTagFromOffer(loadedOffer.getId(), tag2);

        Assert.assertEquals(1, loadedOffer.getTags().size());

        offerService.delete(loadedOffer.getId());
    }

}

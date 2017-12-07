import com.netcracker.parfenenko.Application;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CategoryTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OfferService offerService;

    private long categoryId;
    private final String NAME_1 = "Test category 1";
    private final String NAME_2 = "Test category 2";
    private final String NAME_3 = "Test category 3";
    private final String UPDATED_NAME = "Updated category 1";

    private final String OFFER_NAME_1 = "Test offer 1";
    private final String OFFER_NAME_2 = "Test offer 2";
    private final String OFFER_NAME_3 = "Test offer 3";

    @Before
    public void initCategory() {
        try {
            Category category = new Category();
            category.setName(NAME_1);

            category = categoryService.save(category);
            categoryId = category.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroyCategory() {
        try {
            categoryService.delete(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveTest() {
        try {
            Category category = new Category();
            category.setName(NAME_2);

            category = categoryService.save(category);
            long testCategoryId = category.getId();

            Category loadedCategory = categoryService.findById(testCategoryId);

            Assert.assertEquals(testCategoryId, loadedCategory.getId());
            Assert.assertEquals(NAME_2, loadedCategory.getName());

            categoryService.delete(loadedCategory.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByIdTest() {
        try {
            Category loadedCategory = categoryService.findById(categoryId);

            Assert.assertEquals(categoryId, loadedCategory.getId());
            Assert.assertEquals(NAME_1, loadedCategory.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByNameTest() {
        try {
            Category loadedCategory = categoryService.findByName(NAME_1);

            Assert.assertEquals(categoryId, loadedCategory.getId());
            Assert.assertEquals(NAME_1, loadedCategory.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findAllTest() {
        try {
            int currentSize = categoryService.findAll().size();

            Category category = new Category();
            category.setName(NAME_2);

            category = categoryService.save(category);
            long testCategoryId = category.getId();

            Assert.assertEquals(currentSize + 1, categoryService.findAll().size());

            categoryService.delete(testCategoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest() {
        try {
            Category category = new Category();
            category.setName(UPDATED_NAME);
            category.setId(categoryId);

            category = categoryService.update(category);
            Category loadedCategory = categoryService.findById(categoryId);

            Assert.assertEquals(category.getId(), loadedCategory.getId());
            Assert.assertEquals(UPDATED_NAME, loadedCategory.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTest() {
        try {
            Category category = new Category();
            category.setName(NAME_2);

            category = categoryService.save(category);
            long testCategoryId = category.getId();
            categoryService.delete(testCategoryId);

            Assert.assertNull(categoryService.findById(testCategoryId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findOffersTest() {
        try {
            Category category1 = new Category();
            category1.setName(NAME_2);
            category1 = categoryService.save(category1);

            Category category2 = new Category();
            category2.setName(NAME_3);
            category2 = categoryService.save(category2);

            Offer offer1 = new Offer();
            offer1.setName(OFFER_NAME_1);
            offer1.setCategory(category1);
            offer1 = offerService.save(offer1);

            Offer offer2 = new Offer();
            offer2.setName(OFFER_NAME_2);
            offer2.setCategory(category1);
            offer2 = offerService.save(offer2);

            Offer offer3 = new Offer();
            offer3.setName(OFFER_NAME_3);
            offer3.setCategory(category2);
            offer3 = offerService.save(offer3);

            List<Offer> offers = categoryService.findCategoryOffers(category1.getId());

            Assert.assertEquals(2, offers.size());
            Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
            Assert.assertEquals(OFFER_NAME_2, offers.get(1).getName());

            offerService.delete(offer1.getId());
            offerService.delete(offer2.getId());
            offerService.delete(offer3.getId());
            categoryService.delete(category1.getId());
            categoryService.delete(category2.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addOfferToCategoryTest() {
        try {
            Category category = categoryService.findById(categoryId);

            Offer offer1 = new Offer();
            offer1.setName(OFFER_NAME_1);
            offer1.setCategory(category);
            offer1 = offerService.save(offer1);

            List<Offer> offers = categoryService.findCategoryOffers(categoryId);

            Assert.assertEquals(1, offers.size());

            Offer offer2 = new Offer();
            offer2.setName(OFFER_NAME_2);
            offer2 = offerService.save(offer2);
            categoryService.addOffer(category.getId(), offer2.getId());

            offers = categoryService.findCategoryOffers(categoryId);

            Assert.assertEquals(2, offers.size());
            Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
            Assert.assertEquals(OFFER_NAME_2, offers.get(1).getName());

            offerService.delete(offer1.getId());
            offerService.delete(offer2.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void removeOfferFromCategoryTest() {
        try {
            Category category = categoryService.findById(categoryId);

            Offer offer1 = new Offer();
            offer1.setName(OFFER_NAME_1);
            offer1.setCategory(category);
            offer1 = offerService.save(offer1);

            Offer offer2 = new Offer();
            offer2.setName(OFFER_NAME_2);
            offer2.setCategory(category);
            offer2 = offerService.save(offer2);

            List<Offer> offers = categoryService.findCategoryOffers(categoryId);

            Assert.assertEquals(2, offers.size());

            categoryService.removeOffer(categoryId, offer2.getId());

            offers = categoryService.findCategoryOffers(categoryId);

            Assert.assertEquals(1, offers.size());
            Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());

            offerService.delete(offer1.getId());
            offerService.delete(offer2.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

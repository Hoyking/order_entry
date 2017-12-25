import com.netcracker.parfenenko.CatalogApplication;
import com.netcracker.parfenenko.entities.Category;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Price;
import com.netcracker.parfenenko.entities.Tag;
import com.netcracker.parfenenko.service.CategoryService;
import com.netcracker.parfenenko.service.OfferService;
import com.netcracker.parfenenko.service.TagService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogApplication.class)
public class TagTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private CategoryService categoryService;

    private long tagId;
    private final String NAME_1 = "Test tag 1";
    private final String NAME_2 = "Test tag 2";
    private final String NAME_3 = "Test tag 3";
    private final String UPDATED_NAME = "Updated tag 1";

    private final String OFFER_NAME_1 = "Test offer 1";
    private final String OFFER_NAME_2 = "Test offer 2";
    private final String OFFER_NAME_3 = "Test offer 3";

    private final String CATEGORY_NAME = "Test category name";

    private final String OFFER_DESCRIPTION = "Test offer description";

    private final double PRICE = 3.99;

    @Before
    public void initTag() {
        Tag tag = new Tag();
        tag.setName(NAME_1);

        tag = tagService.save(tag);
        tagId = tag.getId();
    }

    @After
    public void destroyTag() {
        tagService.delete(tagId);
    }

    @Test
    public void saveTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);

        tag = tagService.save(tag);
        long testTagId = tag.getId();

        Tag loadedTag = tagService.findById(testTagId);

        Assert.assertEquals(testTagId, loadedTag.getId());
        Assert.assertEquals(NAME_2, loadedTag.getName());

        tagService.delete(loadedTag.getId());
    }

    @Test
    public void findByIdTest() {
        Tag loadedTag = tagService.findById(tagId);

        Assert.assertEquals(tagId, loadedTag.getId());
        Assert.assertEquals(NAME_1, loadedTag.getName());
    }

    @Test
    public void findByNameTest() {
        Tag loadedTag = tagService.findByName(NAME_1);

        Assert.assertEquals(NAME_1, loadedTag.getName());
    }

    @Test
    public void findAllTest() {
        int currentSize = tagService.findAll().size();

        Tag tag = new Tag();
        tag.setName(NAME_2);

        tag = tagService.save(tag);
        long testTagId = tag.getId();

        Assert.assertEquals(currentSize + 1, tagService.findAll().size());

        tagService.delete(testTagId);
    }

    @Test
    public void updateTest() {
        Tag tag = tagService.findById(tagId);
        tag.setName(UPDATED_NAME);

        tag = tagService.update(tag);
        Tag loadedTag = tagService.findById(tagId);

        Assert.assertEquals(tag.getId(), loadedTag.getId());
        Assert.assertEquals(UPDATED_NAME, loadedTag.getName());
    }

    @Test
    public void deleteTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);
        tag = tagService.save(tag);

        long testTagId = tag.getId();
        tagService.delete(testTagId);

        try {
            tag = tagService.findById(testTagId);
        } catch (EntityNotFoundException e) {
            tag = null;
        }

        Assert.assertNull(tag);
    }

    @Test
    public void findOffersTest() {
        Tag tag1 = new Tag();
        tag1.setName(NAME_2);

        Tag tag2 = new Tag();
        tag2.setName(NAME_3);

        Price price1 = new Price();
        price1.setValue(PRICE);

        Price price2 = new Price();
        price2.setValue(PRICE);

        Price price3 = new Price();
        price3.setValue(PRICE);

        Category category = new Category();
        category.setName(CATEGORY_NAME);
        category = categoryService.save(category);

        Offer offer1 = new Offer();
        offer1.setName(OFFER_NAME_1);
        offer1.setDescription(OFFER_DESCRIPTION);
        offer1.setPrice(price1);
        offer1.setTags(new HashSet<>(Collections.singletonList(tag1)));
        offer1.setCategory(category);
        offer1 = offerService.save(offer1);

        Offer offer2 = new Offer();
        offer2.setName(OFFER_NAME_2);
        offer2.setDescription(OFFER_DESCRIPTION);
        offer2.setPrice(price2);
        offer2.setTags(new HashSet<>(Collections.singletonList(tag1)));
        offer2.setCategory(category);
        offer2 = offerService.save(offer2);

        Offer offer3 = new Offer();
        offer3.setName(OFFER_NAME_3);
        offer3.setDescription(OFFER_DESCRIPTION);
        offer3.setPrice(price3);
        offer3.setTags(new HashSet<>(Collections.singletonList(tag2)));
        offer3.setCategory(category);
        offer3 = offerService.save(offer3);

        List<Offer> offers1 = tagService.findTagOffers(tag1.getName());
        List<Offer> offers2 = tagService.findTagOffers(tag2.getName());

        Assert.assertEquals(2, offers1.size());
        Assert.assertEquals(1, offers2.size());


        offerService.delete(offer1.getId());
        offerService.delete(offer2.getId());
        offerService.delete(offer3.getId());
        categoryService.delete(category.getId());
    }

}

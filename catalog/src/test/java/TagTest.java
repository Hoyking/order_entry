import com.netcracker.parfenenko.Application;
import com.netcracker.parfenenko.entities.Offer;
import com.netcracker.parfenenko.entities.Tag;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TagTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private OfferService offerService;

    private long tagId;
    private final String NAME_1 = "Test tag 1";
    private final String NAME_2 = "Test tag 2";
    private final String NAME_3 = "Test tag 3";
    private final String UPDATED_NAME = "Updated tag 1";

    private final String OFFER_NAME_1 = "Test offer 1";
    private final String OFFER_NAME_2 = "Test offer 2";
    private final String OFFER_NAME_3 = "Test offer 3";

    @Before
    public void initTag() {
        try {
            Tag tag = new Tag();
            tag.setName(NAME_1);

            tag = tagService.save(tag);
            tagId = tag.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroyTag() {
        try {
            tagService.delete(tagId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveTest() {
        try {
            Tag tag = new Tag();
            tag.setName(NAME_2);

            tag = tagService.save(tag);
            long testTagId = tag.getId();

            Tag loadedTag = tagService.findById(testTagId);

            Assert.assertEquals(testTagId, loadedTag.getId());
            Assert.assertEquals(NAME_2, loadedTag.getName());

            tagService.delete(loadedTag.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByIdTest() {
        try {
            Tag loadedTag = tagService.findById(tagId);

            Assert.assertEquals(tagId, loadedTag.getId());
            Assert.assertEquals(NAME_1, loadedTag.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByNameTest() {
        try {
            Tag loadedTag = tagService.findByName(NAME_1);

            Assert.assertEquals(tagId, loadedTag.getId());
            Assert.assertEquals(NAME_1, loadedTag.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findAllTest() {
        try {
            int currentSize = tagService.findAll().size();

            Tag tag = new Tag();
            tag.setName(NAME_2);

            tag = tagService.save(tag);
            long testTagId = tag.getId();

            Assert.assertEquals(currentSize + 1, tagService.findAll().size());

            tagService.delete(testTagId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest() {
        try {
            Tag tag = new Tag();
            tag.setName(UPDATED_NAME);
            tag.setId(tagId);

            tag = tagService.update(tag);
            Tag loadedTag = tagService.findById(tagId);

            Assert.assertEquals(tag.getId(), loadedTag.getId());
            Assert.assertEquals(UPDATED_NAME, loadedTag.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTest() {
        try {
            Tag tag = new Tag();
            tag.setName(NAME_2);

            tag = tagService.save(tag);
            long testTagId = tag.getId();
            tagService.delete(testTagId);

            Assert.assertNull(tagService.findById(testTagId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findOffersTest() {
        try {
            Tag tag1 = new Tag();
            tag1.setName(NAME_2);

            Tag tag2 = new Tag();
            tag2.setName(NAME_3);

            Offer offer1 = new Offer();
            offer1.setName(OFFER_NAME_1);
            offer1.setTags(new HashSet<>(Collections.singletonList(tag1)));
            offer1 = offerService.save(offer1);

            Offer offer2 = new Offer();
            offer2.setName(OFFER_NAME_2);
            offer2.setTags(new HashSet<>(Collections.singletonList(tag1)));
            offer2 = offerService.save(offer2);

            Offer offer3 = new Offer();
            offer3.setName(OFFER_NAME_3);
            offer3.setTags(new HashSet<>(Collections.singletonList(tag2)));
            offer3 = offerService.save(offer3);

            List<Offer> offers = tagService.findTagOffers(tag1.getName());

            Assert.assertEquals(2, offers.size());
            Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
            Assert.assertEquals(OFFER_NAME_2, offers.get(1).getName());

            offerService.delete(offer1.getId());
            offerService.delete(offer2.getId());
            offerService.delete(offer3.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

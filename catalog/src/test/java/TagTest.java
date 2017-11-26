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

        Assert.assertEquals(tagId, loadedTag.getId());
        Assert.assertEquals(NAME_1, loadedTag.getName());
    }

    @Test
    public void findAllTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);

        tag = tagService.save(tag);
        long testTagId = tag.getId();

        Assert.assertEquals(2, tagService.findAll().size());

        tagService.delete(testTagId);
    }

    @Test
    public void updateTest() {
        Tag tag = new Tag();
        tag.setName(UPDATED_NAME);
        tag.setId(tagId);

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

        Assert.assertNull(tagService.findById(testTagId));
    }

    @Test
    public void findOffersTest() {
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

        List<Offer> offers = tagService.findTagOffers(tag1.getId());

        Assert.assertEquals(2, offers.size());
        Assert.assertEquals(OFFER_NAME_1, offers.get(0).getName());
        Assert.assertEquals(OFFER_NAME_2, offers.get(1).getName());

        offerService.delete(offer1.getId());
        offerService.delete(offer2.getId());
        offerService.delete(offer3.getId());
    }

}

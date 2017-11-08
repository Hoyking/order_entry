import com.netcracker.parfenenko.dao.TagDAO;
import com.netcracker.parfenenko.entities.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TagTest {

    private long tagId;
    private final String NAME_1 = "Test tag 1";
    private final String NAME_2 = "Test tag 2";
    private final String UPDATED_NAME = "Updated tag 1";

    private static TagDAO tagDAO;

    @Before
    public void initTag() {
        Tag tag = new Tag();
        tag.setName(NAME_1);

        tagId = tagDAO.save(tag);
    }

    @After
    public void destroyTag() {
        tagDAO.delete(tagId);
    }

    @Test
    public void saveTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);

        long testTagId = tagDAO.save(tag);
        tag.setId(testTagId);

        Tag loadedTag = tagDAO.findById(testTagId);

        Assert.assertEquals(testTagId, loadedTag.getId());
        Assert.assertEquals(NAME_2, loadedTag.getName());
    }

    @Test
    public void findByIdTest() {
        Tag loadedTag = tagDAO.findById(tagId);

        Assert.assertEquals(tagId, loadedTag.getId());
        Assert.assertEquals(NAME_1, loadedTag.getName());
    }

    @Test
    public void findByNameTest() {
        Tag loadedTag = tagDAO.findByName(NAME_1);

        Assert.assertEquals(tagId, loadedTag.getId());
        Assert.assertEquals(NAME_1, loadedTag.getName());
    }

    @Test
    public void findAllTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);

        long testTagId = tagDAO.save(tag);

        Assert.assertEquals(2, tagDAO.findAll().size());

        tagDAO.delete(testTagId);
    }

    @Test
    public void updateTest() {
        Tag tag = new Tag();
        tag.setName(UPDATED_NAME);
        tag.setId(tagId);

        tagDAO.update(tag);
        Tag loadedTag = tagDAO.findById(tagId);

        Assert.assertEquals(tagId, loadedTag.getId());
        Assert.assertEquals(UPDATED_NAME, loadedTag.getName());
    }

    @Test
    public void deleteTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);

        long testTagId = tagDAO.save(tag);
        tagDAO.delete(testTagId);

        Assert.assertNull(tagDAO.findById(testTagId));
    }

}

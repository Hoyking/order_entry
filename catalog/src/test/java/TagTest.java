import com.netcracker.parfenenko.dao.JPATagDAO;
import com.netcracker.parfenenko.dao.TagDAO;
import com.netcracker.parfenenko.entities.Tag;
import org.junit.*;

public class TagTest {

    private long tagId;
    private final String NAME_1 = "Test tag 1";
    private final String NAME_2 = "Test tag 2";
    private final String UPDATED_NAME = "Updated tag 1";

    private static TagDAO tagDAO;

    @BeforeClass
    public static void init() {
        tagDAO = JPATagDAO.getInstance();
    }

    @Before
    public void initTag() {
        Tag tag = new Tag();
        tag.setName(NAME_1);

        tag = tagDAO.save(tag);
        tagId = tag.getId();
    }

    @After
    public void destroyTag() {
        tagDAO.delete(tagId);
    }

    @Test
    public void saveTest() {
        Tag tag = new Tag();
        tag.setName(NAME_2);

        tag = tagDAO.save(tag);
        long testTagId = tag.getId();

        Tag loadedTag = tagDAO.findById(testTagId);

        Assert.assertEquals(testTagId, loadedTag.getId());
        Assert.assertEquals(NAME_2, loadedTag.getName());

        tagDAO.delete(loadedTag.getId());
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

        tag = tagDAO.save(tag);
        long testTagId = tag.getId();

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

        tag = tagDAO.save(tag);
        long testTagId = tag.getId();
        tagDAO.delete(testTagId);

        Assert.assertNull(tagDAO.findById(testTagId));
    }

}

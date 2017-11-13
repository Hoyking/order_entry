import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.dao.JPACategoryDAO;
import com.netcracker.parfenenko.entities.Category;
import org.junit.*;

public class CategoryTest {

    private long categoryId;
    private final String NAME_1 = "Test category 1";
    private final String NAME_2 = "Test category 2";
    private final String UPDATED_NAME = "Updated category 1";

    private static CategoryDAO categoryDAO;

    @BeforeClass
    public static void init() {
        categoryDAO = JPACategoryDAO.getInstance();
    }

    @Before
    public void initCategory() {
        Category category = new Category();
        category.setName(NAME_1);

        category = categoryDAO.save(category);
        categoryId = category.getId();
    }

    @After
    public void destroyCategory() {
        categoryDAO.delete(categoryId);
    }

    @Test
    public void saveTest() {
        Category category = new Category();
        category.setName(NAME_2);

        category = categoryDAO.save(category);
        long testCategoryId = category.getId();

        Category loadedCategory = categoryDAO.findById(testCategoryId);

        Assert.assertEquals(testCategoryId, loadedCategory.getId());
        Assert.assertEquals(NAME_2, loadedCategory.getName());

        categoryDAO.delete(loadedCategory.getId());
    }

    @Test
    public void findByIdTest() {
        Category loadedCategory = categoryDAO.findById(categoryId);

        Assert.assertEquals(categoryId, loadedCategory.getId());
        Assert.assertEquals(NAME_1, loadedCategory.getName());
    }

    @Test
    public void findByNameTest() {
        Category loadedCategory = categoryDAO.findByName(NAME_1);

        Assert.assertEquals(categoryId, loadedCategory.getId());
        Assert.assertEquals(NAME_1, loadedCategory.getName());
    }

    @Test
    public void findAllTest() {
        Category category = new Category();
        category.setName(NAME_2);

        category = categoryDAO.save(category);
        long testCategoryId = category.getId();

        Assert.assertEquals(2, categoryDAO.findAll().size());

        categoryDAO.delete(testCategoryId);
    }

    @Test
    public void updateTest() {
        Category category = new Category();
        category.setName(UPDATED_NAME);
        category.setId(categoryId);

        categoryDAO.update(category);
        Category loadedCategory = categoryDAO.findById(categoryId);

        Assert.assertEquals(categoryId, loadedCategory.getId());
        Assert.assertEquals(UPDATED_NAME, loadedCategory.getName());
    }

    @Test
    public void deleteTest() {
        Category category = new Category();
        category.setName(NAME_2);

        category = categoryDAO.save(category);
        long testCategoryId = category.getId();
        categoryDAO.delete(testCategoryId);

        Assert.assertNull(categoryDAO.findById(testCategoryId));
    }

}

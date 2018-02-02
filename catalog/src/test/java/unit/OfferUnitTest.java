package unit;

import com.netcracker.parfenenko.CatalogApplication;
import com.netcracker.parfenenko.dao.CategoryDAO;
import com.netcracker.parfenenko.dao.OfferDAO;
import com.netcracker.parfenenko.dao.PriceDAO;
import com.netcracker.parfenenko.entity.Category;
import com.netcracker.parfenenko.entity.Offer;
import com.netcracker.parfenenko.exception.EntityCreationException;
import com.netcracker.parfenenko.service.OfferService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogApplication.class)
public class OfferUnitTest {

    @Mock
    private OfferDAO offerDAO;
    @Mock
    private CategoryDAO categoryDAO;
    @Mock
    private PriceDAO priceDAO;
    @InjectMocks
    private OfferService offerService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(OfferUnitTest.class);
    }

    @Test
    public void saveTest() {
        Offer offer = mock(Offer.class);
        Category category = mock(Category.class);

        when(offer.getCategory()).thenReturn(category);
        when(categoryDAO.findById(offer.getCategory().getId())).thenReturn(category);
        when(offerDAO.findByName(offer.getName())).thenThrow(EntityNotFoundException.class);
        when(offerDAO.save(offer)).thenReturn(offer);

        offerService.save(offer);

        InOrder inOrder = Mockito.inOrder(categoryDAO, offer, offerDAO);
        inOrder.verify(categoryDAO).findById(offer.getCategory().getId());
        inOrder.verify(offer).setCategory(category);
        inOrder.verify(offerDAO).findByName(offer.getName());
        inOrder.verify(offerDAO).save(offer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveWithWrongCategoryTest() {
        Offer offer = mock(Offer.class);
        Category category = mock(Category.class);

        when(offer.getCategory()).thenReturn(category);
        when(categoryDAO.findById(offer.getCategory().getId())).thenThrow(EntityNotFoundException.class);

        offerService.save(offer);
    }

    @Test(expected = EntityCreationException.class)
    public void saveExistedOfferTest() {
        Offer offer = mock(Offer.class);
        Category category = mock(Category.class);

        when(offer.getCategory()).thenReturn(category);
        when(categoryDAO.findById(offer.getCategory().getId())).thenReturn(category);

        offerService.save(offer);
    }

    @Test
    public void findByFilterTest() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("categories", Collections.singletonList(0 + ""));
        filters.put("tags", Collections.singletonList("Test tag"));
        filters.put("from", Collections.singletonList(2.99 + ""));
        filters.put("to", Collections.singletonList(4.99 + ""));

        offerService.findByFilter(filters);

        verify(offerDAO).findByFilters(Mockito.anyList(), Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByWrongFilterTest() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("categories", Collections.singletonList(0 + ""));
        filters.put("tags", Collections.singletonList("Test tag"));
        filters.put("from", Collections.singletonList(4.99 + ""));
        filters.put("to", Collections.singletonList(2.99 + ""));

        try {
            offerService.findByFilter(filters);
        } catch (IllegalArgumentException e) {
            verify(offerDAO, never()).findByFilters(Mockito.anyList(), Mockito.anyList(), Mockito.anyDouble(), Mockito.anyDouble());
            throw e;
        }
    }

}

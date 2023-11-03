import org.example.Session;
import org.example.UserInterface;
import org.example.dao.BaseDaoTests;
import org.example.dao.JdbcCustomerDao;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionTests extends BaseDaoTests {
    private final UserInterface ui = new UserInterface();
    private final Session s = new Session(ui);
    private JdbcCustomerDao testCd;
    private static final Customer CUSTOMER_1 = new Customer(1,"Russell Hoffman","admin");
    private static final Customer CUSTOMER_2 = new Customer(2, "Mike Levy","test123");
    @Before
    public void setup(){
         testCd = new JdbcCustomerDao(dataSource);

    }
    @Test
    public void create_customer_creates_new_customer(){
        s.setName("Mike Levy");
        s.setPassword("test456");
        Customer newCustomer = s.createCustomer();
        Customer retrieved = testCd.createCustomer(newCustomer);
        Assert.assertEquals("If user is new, authenticate should create new user", CUSTOMER_2,retrieved);

    }
    @Test
    public void when_username_and_password_found_access_correct_customer(){
        Customer actual = testCd.getCustomerByNameAndPassword("Russell Hoffman","admin");
        Assert.assertEquals("If user is returning, return correct user", CUSTOMER_1,actual);
    }
    @Test
    public void when_look_up_valid_customer_then_return_customer(){
        s.setName("Russell Hoffman");
        s.setPassword("admin");
        Customer expected = new Customer();
        expected.setName("Russell Hoffman");
        expected.setPassword("admin");
        Customer retrieved = s.lookUpCustomer();
        Assert.assertEquals("Lookup valid customer should return customer",expected,retrieved);
    }

}

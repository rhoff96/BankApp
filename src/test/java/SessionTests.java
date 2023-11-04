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
    public void sample(){


    }

}

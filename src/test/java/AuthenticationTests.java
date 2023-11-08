import org.example.workflow.Authentication;
import org.example.dao.BaseDaoTests;
import org.example.dao.CustomerDao;
import org.example.dao.JdbcCustomerDao;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AuthenticationTests extends BaseDaoTests {

    private CustomerDao testCd;
    private static final Customer CUSTOMER_1 = new Customer(1, "Russell Hoffman", "admin");

    private static final Customer CUSTOMER_2 = new Customer(3,"New User","newpasswordentry");
    @Before
    public void setup() {
        testCd = new JdbcCustomerDao(dataSource);
    }

    @Test
    public void when_password_invalid_return_null_customer() {
        Authentication authentication = new Authentication("Russell Hoffman", "wrongpassword", testCd, false);
        Customer retrieved = authentication.authenticate();
        Assert.assertNull(retrieved);
    }

    @Test
    public void when_user_and_password_correct_return_correct_customer() {
        Authentication authentication = new Authentication("Russell Hoffman","admin",testCd,false);
        Customer retrieved = authentication.authenticate();
        CUSTOMER_1.setTotalBalance(new BigDecimal("710.0"));
        Assert.assertEquals("Valid username and password should return correct customer",CUSTOMER_1,retrieved);
    }
    @Test
    public void when_user_new_create_correct_account(){
        Authentication authentication = new Authentication("New User","newpasswordentry",testCd,true);
        Customer retrieved = authentication.authenticate();
        CUSTOMER_2.setTotalBalance(BigDecimal.ZERO);
        Assert.assertEquals("Authenticate should create correct profile for new customer",CUSTOMER_2,retrieved);


    }
}

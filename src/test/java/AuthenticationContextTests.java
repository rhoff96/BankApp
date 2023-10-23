import org.example.Session;
import org.example.UserInterface;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticationContextTests {
    private final UserInterface ui = new UserInterface();
    private final Session ac = new Session(ui);

    @Test
    public void when_customer_new_then_create_new_customer(){
        Customer expected = new Customer(1,"Mike Levy","test123");
        ac.setName("Mike Levy");
        ac.setPassword("test123");
        Customer actual = ac.createCustomer();
        Assert.assertEquals("If user is new, authenticate should create new user", expected,actual);

    }
//    @Test
//    public void when_username_and_password_found_access_correct_user(){
//        Customer expected = new Customer("Russell Hoffman","admin", ui);
//        ac.createUser("Russell Hoffman","admin");
//        User actual = ac.lookUpUser("Russell Hoffman", "admin");
//        Assert.assertEquals("If user is returning, return correct user", expected,actual);
//    }
}

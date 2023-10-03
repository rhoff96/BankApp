import org.example.AuthenticationContext;
import org.example.User;
import org.example.UserInterface;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticationContextTests {
    UserInterface ui = new UserInterface();
    AuthenticationContext ac = new AuthenticationContext(ui);

    @Test
    public void when_user_new_then_authenticate_creates_new_user(){
        User expected = new User("Mike Levy","test123", ui);
        User actual = ac.authenticate("Mike Levy", "test123");
        Assert.assertEquals("If user is new, authenticate should create new user", expected,actual);

    }
    @Test
    public void when_username_and_password_found_access_correct_user(){
        User expected = new User("Russell Hoffman","admin", ui);
        User actual = ac.authenticate("Russell Hoffman", "admin");
        Assert.assertEquals("If user is returning, return correct user", expected,actual);
    }
}

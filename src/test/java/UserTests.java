import org.example.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class UserTests {

    private UserInterface ui;
    private AuthenticationContext ac = new AuthenticationContext(ui);
    private User u = new User("test","tester",ui);
    private Account a = new CheckingAccount("Checking",123,ui);
    @Test
    public void when_user_has_total_balance_1000_then_bronze() {
        a.deposit(new BigDecimal("1000"));
        u.setTier(u);
        User.Tier actual = u.getTier();
        User.Tier expected = User.Tier.Bronze;
        Assert.assertEquals("Total Balance of $1000 should return Bronze Tier", expected,actual);
    }

//    @Test
//    public void when_user_has_total_balance_7000_then_silver() {
//        a.deposit(new BigDecimal("7000"));
//        u.setTier(u);
//        User.Tier actual = u.getTier();
//        User.Tier expected = User.Tier.Silver;
//        Assert.assertEquals("Total Balance of $7000 should return Silver Tier", expected,actual);
//
//    }
//
//    @Test
//    public void when_user_has_total_balance_12000_then_gold() {
//
//        a.deposit(new BigDecimal("12000"));
//        u.setTier(u);
//        User.Tier actual = u.getTier();
//        User.Tier expected = User.Tier.Gold;
//        Assert.assertEquals("Total Balance of $12000 should return Gold Tier", expected,actual);
//
//    }

    @Test
    public void when_user_has_total_balance_30000_then_platinum() {

    }

    @Test
    public void when_user_has_total_5000_then_silver() {


    }
    @Test
    public void when_user_has_total_10000_then_gold(){

    }
    @Test
    public void when_multiple_accounts_then_correct_total(){

    }
    @Test
    public void when_one_of_multiple_is_empty_then_correct_total(){

    }
}

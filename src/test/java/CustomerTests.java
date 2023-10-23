import org.example.*;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CustomerTests {

    private UserInterface ui;
    private Session ac = new Session(ui);
    private Customer c = new Customer(1,"tester","password");
    private Account a = new CheckingAccount("Checking",1);
    @Test
    public void when_customer_has_total_balance_1000_then_bronze() {
        a.deposit(new BigDecimal("1000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Bronze;
        Assert.assertEquals("Total Balance of $1000 should return Bronze Tier", expected,actual);
    }

    @Test
    public void when_customer_has_total_balance_7000_then_silver() {
        a.deposit(new BigDecimal("7000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Silver;
        Assert.assertEquals("Total Balance of $7000 should return Silver Tier", expected,actual);

    }

    @Test
    public void when_customer_has_total_balance_12000_then_gold() {
        a.deposit(new BigDecimal("12000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Gold;
        Assert.assertEquals("Total Balance of $12000 should return Gold Tier", expected,actual);

    }

    @Test
    public void when_customer_has_total_balance_30000_then_platinum() {
        a.deposit(new BigDecimal("30000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Platinum;
        Assert.assertEquals("Total Balance of $30000 should return Platinum Tier", expected,actual);

    }

    @Test
    public void when_customer_has_total_5000_then_silver() {
        a.deposit(new BigDecimal("5000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Silver;
        Assert.assertEquals("Total Balance of $5000 should return Silver Tier", expected,actual);


    }
    @Test
    public void when_customer_has_total_10000_then_gold(){
        a.deposit(new BigDecimal("10000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Gold;
        Assert.assertEquals("Total Balance of $10000 should return Gold Tier", expected,actual);

    }
//    @Test
//    public void when_multiple_accounts_then_correct_total(){
//        u.currentAccount = a;
//        Account sa = new SavingsAccount("Savings",234,ui);
//        sa.setBalance(new BigDecimal("2500"));
//        u.userAccounts.add(a);
//        u.userAccounts.add(sa);
//        a.deposit(new BigDecimal("3000"));
//        u.setTier();
//        User.Tier actual = u.getTier();
//        User.Tier expected = User.Tier.Silver;
//        Assert.assertEquals("Total Balance of $5500 should return Silver Tier", expected,actual);
//
//    }
//    @Test
//    public void when_one_of_multiple_is_empty_then_correct_total(){
//        u.currentAccount = a;
//        Account sa = new SavingsAccount("Savings",235,ui);
//        sa.setBalance(BigDecimal.ZERO);
//        u.userAccounts.add(a);
//        a.deposit(new BigDecimal("12000"));
//        u.setTier();
//        User.Tier actual = u.getTier();
//        User.Tier expected = User.Tier.Gold;
//        Assert.assertEquals("Total Balance of $12000 should return Gold Tier", expected,actual);
//
//    }
}

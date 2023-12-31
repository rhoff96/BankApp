import org.example.*;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CustomerTests {
    private Customer c = new Customer(1, "tester", "password");
    private org.example.model.Account a = new org.example.model.Account(1,1, "Checking");

    @Test
    public void when_customer_has_total_balance_1000_then_set_tier_bronze() {
        c.setTotalBalance(new BigDecimal("1000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Bronze;
        Assert.assertEquals("Total Balance of $1000 should return Bronze Tier", expected, actual);
    }

    @Test
    public void when_customer_has_total_balance_7000_then_silver() {
        c.setTotalBalance(new BigDecimal("7000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Silver;
        Assert.assertEquals("Total Balance of $7000 should return Silver Tier", expected, actual);

    }

    @Test
    public void when_customer_has_total_balance_12000_then_gold() {
        c.setTotalBalance(new BigDecimal("12000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Gold;
        Assert.assertEquals("Total Balance of $12000 should return Gold Tier", expected, actual);

    }

    @Test
    public void when_customer_has_total_balance_30000_then_platinum() {
        c.setTotalBalance(new BigDecimal("30000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Platinum;
        Assert.assertEquals("Total Balance of $30000 should return Platinum Tier", expected, actual);

    }

    @Test
    public void when_customer_has_total_5000_then_silver() {
        c.setTotalBalance(new BigDecimal("5000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Silver;
        Assert.assertEquals("Total Balance of $5000 should return Silver Tier", expected, actual);


    }

    @Test
    public void when_customer_has_total_10000_then_gold() {
        c.setTotalBalance(new BigDecimal("10000"));
        c.setTier();
        Customer.Tier actual = c.getTier();
        Customer.Tier expected = Customer.Tier.Gold;
        Assert.assertEquals("Total Balance of $10000 should return Gold Tier", expected, actual);
    }

}

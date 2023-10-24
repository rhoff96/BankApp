package org.example.dao;

import org.example.Session;
import org.example.UserInterface;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JdbcCustomerDaoTests {
    private final UserInterface ui = new UserInterface();
    private final Session s = new Session(ui);
    private JdbcCustomerDao testCd;
    private static final Customer CUSTOMER_1 = new Customer(1,"Russell Hoffman","admin");
    private static final Customer CUSTOMER_2 = new Customer(2, "Mike Levy","test123");

    @Before
    public void setup(){
//        testCd = new JdbcCustomerDao(dataSource);
    }
    @Test
    public void create_customer_creates_new_customer(){
        s.setName("Mike Levy");
        s.setPassword("test456");
        Customer newCustomer = s.createCustomer();
        Customer retrieved = testCd.createCustomer(newCustomer);
        Assert.assertEquals("Create customer should return new customer", CUSTOMER_2,retrieved);

    }
}
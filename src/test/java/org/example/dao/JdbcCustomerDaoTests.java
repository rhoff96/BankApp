package org.example.dao;

import org.example.model.Account;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerDaoTests extends BaseDaoTests {
    private JdbcCustomerDao testCd;
    private static final Customer CUSTOMER_1 = new Customer(1, "Russell Hoffman", "admin");
    private static final Customer CUSTOMER_2 = new Customer(2, "Mike Levy", "test123");

    @Before
    public void setup() {
        testCd = new JdbcCustomerDao(dataSource);
    }


    @Test
    public void get_customer_by_id_with_valid_id_returns_correct_customer() {
        CUSTOMER_1.setTotalBalance(new BigDecimal("710.0"));
        Customer retrieved = testCd.getCustomerById(1);
        Assert.assertEquals("Customers with the same id should be equal", CUSTOMER_1, retrieved);
    }

    @Test
    public void get_customer_by_invalid_id_returns_null() {
        Customer retrieved = testCd.getCustomerById(0);
        Assert.assertNull("Invlaid customer id should return null", retrieved);
    }

    @Test
    public void get_customer_by_valid_name_password_returns_customer() {
        CUSTOMER_1.setTotalBalance(new BigDecimal("710.0"));
        Customer retrieved = testCd.getCustomerByNameAndPassword("Russell Hoffman", "admin");
        Assert.assertEquals("Valid name and password should return customer", CUSTOMER_1, retrieved);
    }

    @Test
    public void get_customer_by_invalid_username_password_returns_null() {
        Customer retrieved = testCd.getCustomerByNameAndPassword("Invalid", "admin");
        Assert.assertNull("Invalid username should return null", retrieved);
    }

    @Test
    public void get_customer_by_name_and_password_with_invalid_password_returns_null() {
        Customer retrieved = testCd.getCustomerByNameAndPassword("Russell Hoffman", "wrong");
        Assert.assertNull("Invalid password should return null", retrieved);
    }

    @Test
    public void create_customer_creates_new_customer() {
        Customer newCustomer = new Customer();
        newCustomer.setName("Mike Levy");
        newCustomer.setPassword("test123");
        CUSTOMER_2.setTotalBalance(BigDecimal.ZERO);
        Customer retrieved = testCd.createCustomer(newCustomer);
        Assert.assertEquals("Create customer should return new customer", CUSTOMER_2, retrieved);
    }

    @Test
    public void create_customer_with_invalid_data_returns_null() {
        Customer newCustomer = new Customer();
        newCustomer.setName(null);
        newCustomer.setPassword(null);
        Customer retrieved = testCd.createCustomer(newCustomer);
        Assert.assertNull("Null username or password should return null customer", retrieved);
    }

    @Test
    public void update_customer_updates_customer() {
        Customer customerToUpdate = testCd.getCustomerById(1);
        customerToUpdate.setName("New Name");
        customerToUpdate.setPassword("New Password");
        Customer updatedCustomer = testCd.updateCustomer(customerToUpdate);
        Assert.assertNotNull(updatedCustomer);
        Customer retrievedCustomer = testCd.getCustomerById(1);
        Assert.assertEquals(customerToUpdate, retrievedCustomer);
    }

    @Test
    public void delete_customer_deletes_customer() {
        int rowsAffected = testCd.deleteCustomerById(1);
        Assert.assertEquals(1, rowsAffected);

        Customer retrievedCustomer = testCd.getCustomerById(1);
        Assert.assertNull(retrievedCustomer);

    }

    @Test
    public void when_valid_customer_then_get_accounts_by_customer_id_returns_all_accounts() {
        Account account1 = new Account(1, 1, "Checking");
        account1.setAccountBalance(new BigDecimal("10.0"));
        Account account2 = new Account(2, 1, "Savings");
        account2.setAccountBalance(new BigDecimal("700.0"));
        Account account3 = new Account(5,1,"Checking");
        account3.setAccountBalance(BigDecimal.ZERO);
        List<Account> expected = new ArrayList<>();
        expected.add(account1);
        expected.add(account2);
        expected.add(account3);
        List<Account> actual = testCd.getAccountsByCustomerId(1);
        Assert.assertEquals("Valid customer id should return all accounts", expected, actual);
    }
    @Test
    public void when_invalid_customer_then_get_customer_by_customer_id_returns_empty_list(){
        List<Account>expected = new ArrayList<>();
        List<Account> retrieved = testCd.getAccountsByCustomerId(0);
        Assert.assertEquals("Invalid customer id should return an empty list",expected,retrieved);

    }
    @Test
    public void when_username_and_password_found_then_get_customer_by_name_and_passsword_returns_correct_customer() {
        Customer actual = testCd.getCustomerByNameAndPassword("Russell Hoffman", "admin");
        CUSTOMER_1.setTotalBalance(new BigDecimal("710.0"));
        Assert.assertEquals("If user is returning, return correct user", CUSTOMER_1, actual);
    }

    @Test
    public void when_user_name_and_password_not_found_return_null() {
        Customer actual = testCd.getCustomerByNameAndPassword("Invalid Username","invalid password");
        Assert.assertNull("Invalid username or password should return null",actual);

    }
}

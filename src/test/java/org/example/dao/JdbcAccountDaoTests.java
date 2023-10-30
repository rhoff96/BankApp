package org.example.dao;

import org.example.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcAccountDao testAd;
    private static final Account ACCOUNT_1 = new Account(1, 1, "Checking");
    private static final Account ACCOUNT_2 = new Account(5, 1, "Savings");
    private static final Account ACCOUNT_3 = new Account(3, 1, "Checking");

    @Before
    public void setup() {
        testAd = new JdbcAccountDao(dataSource);
    }

    @Test
    public void when_valid_id_then_get_account_by_id_returns_account() {
        Account retrieved = testAd.getAccountById(1);
        Assert.assertEquals("Valid id should return correct account", ACCOUNT_1, retrieved);
    }

    @Test
    public void when_invalid_id_then_get_account_by_id_returns_null() {
        Account retrieved = testAd.getAccountById(0);
        Assert.assertNull(retrieved);

    }

    @Test
    public void when_valid_account_create_account_creates_account() {
        Account newAccount = new Account();
        newAccount.setCustomerId(1);
        newAccount.setAccountType("Savings");
        Account retrieved = testAd.createAccount(newAccount);
        Assert.assertEquals("Valid account should return correct account", ACCOUNT_2, retrieved);
    }
    //invalid account test?

    @Test
    public void when_valid_account_update_account_updates_account() {
        Account accountToUpdate = testAd.getAccountById(1);
        accountToUpdate.setAccountType("Savings");
        accountToUpdate.setCustomerId(2);
        Account updatedAccount = testAd.updateAccount(accountToUpdate);
        Assert.assertNotNull(updatedAccount);
        Account retrievedAccount = testAd.getAccountById(1);
        Assert.assertEquals(accountToUpdate, retrievedAccount);

    }
}

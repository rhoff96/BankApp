package org.example.dao;

import org.example.model.Account;
import org.example.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcAccountDao testAd;
    private static final Account ACCOUNT_1 = new Account(1, 1, "Checking");
    private static final Account ACCOUNT_2 = new Account(7, 1, "Savings", 0);
    private static final Account ACCOUNT_3 = new Account(3, 2, "Checking");
    private static final Account ACCOUNT_4 = new Account(4, 2, "Savings");
    private static final Account ACCOUNT_5 = new Account(5, 1, "Checking");
    private static final Account ACCOUNT_6 = new Account(2, 1, "Savings");



    @Before
    public void setup() {
        testAd = new JdbcAccountDao(dataSource);
    }

    @Test
    public void when_valid_id_then_get_account_by_id_returns_account() {
        ACCOUNT_1.setAccountBalance(new BigDecimal("10.0"));
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
        ACCOUNT_2.setAccountBalance(BigDecimal.ZERO);
        Assert.assertEquals("Valid account should return correct account", ACCOUNT_2, retrieved);
    }
    //invalid account test?

    @Test
    public void when_valid_account_update_account_updates_account() {
        Account accountToUpdate = testAd.getAccountById(1);
        accountToUpdate.setAccountType("Savings");
        accountToUpdate.setCustomerId(2);
        accountToUpdate.setWithdrawalCount(5);
        Account updatedAccount = testAd.updateAccount(accountToUpdate);
        Assert.assertNotNull(updatedAccount);
        Account retrievedAccount = testAd.getAccountById(1);
        Assert.assertEquals(accountToUpdate, retrievedAccount);

    }

    @Test
    public void when_valid_account_then_delete_account_deletes_account() {
        int rowsAffected = testAd.deleteAccountById(1);
        Assert.assertEquals(1, rowsAffected);

        Account retrievedAccount = testAd.getAccountById(1);
        Assert.assertNull(retrievedAccount);

    }

    @Test
    public void get_all_accounts_returns_all_accounts() {
        List<Account> actual = testAd.getAllAccounts();
        List<Account> expected = new ArrayList<>();
        expected.add(ACCOUNT_1);
        ACCOUNT_1.setAccountBalance(new BigDecimal("10.0"));
        expected.add(ACCOUNT_6);
        ACCOUNT_6.setAccountBalance(new BigDecimal("700.0"));
        expected.add(ACCOUNT_3);
        ACCOUNT_3.setAccountBalance(new BigDecimal("50.0"));
        expected.add(ACCOUNT_4);
        ACCOUNT_4.setAccountBalance(new BigDecimal("200.0"));
        expected.add(ACCOUNT_5);
        ACCOUNT_5.setAccountBalance(new BigDecimal("0.0"));


        Assert.assertEquals("Get all accounts should return all accounts",expected,actual);



    }
}

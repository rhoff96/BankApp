package org.example.dao;

import org.example.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDaoTests extends BaseDaoTests {

    private JdbcTransactionDao testTd;
    private static final Transaction TRANSACTION_1 = new Transaction(Timestamp.valueOf("2012-10-25 00:00:00"), new BigDecimal("100.0"), 1, 1, new BigDecimal("10.0"));
    private static final Transaction TRANSACTION_2 = new Transaction(Timestamp.valueOf("2019-09-15 00:00:00"), new BigDecimal("300.0"), 3, 3, new BigDecimal("50.0"));
    private static final Transaction TRANSACTION_3 = new Transaction(Timestamp.valueOf("2023-01-01 00:00:00"), new BigDecimal("50.0"), 4, 4, new BigDecimal("200.0"));

    private static final Transaction TRANSACTION_4 = new Transaction(Timestamp.valueOf("2023-02-02 00:00:00"), new BigDecimal("100.0"), 5, 2, new BigDecimal("200.0"));

    private static final Transaction TRANSACTION_5 = new Transaction(Timestamp.valueOf("2015-11-20 00:00:00"), new BigDecimal("50.0"), 2, 2, new BigDecimal("200.0"));

    @Before
    public void setup() {
        testTd = new JdbcTransactionDao(dataSource);
    }

    @Test
    public void when_valid_id_then_get_transaction_by_id_returns_transaction() {
        Transaction retrieved = testTd.getTransactionById(1);
        Assert.assertEquals("Valid id should return transaction", TRANSACTION_1, retrieved);
    }

    @Test
    public void when_invalid_id_then_get_transaction_by_id_returns_null() {
        Transaction retrieved = testTd.getTransactionById(0);
        Assert.assertNull("Invalid id should return null", retrieved);
    }

    @Test
    public void when_valid_transaction_then_create_transaction_creates_transaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAccountNumber(2);
        newTransaction.setTime(Timestamp.valueOf("2022-09-15 00:00:00"));
        newTransaction.setAmount(BigDecimal.valueOf(200.0));
        newTransaction.setPreviousBalance(BigDecimal.valueOf(100.0));
        Transaction retrieved = testTd.createTransaction(newTransaction);
        Assert.assertEquals("Valid account should return correct account", TRANSACTION_4, retrieved);
    }

    @Test
    public void when_valid_customer_id_then_get_transactions_by_customer_id_returns_transactions() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_3);
        expected.add(TRANSACTION_2);
        List<Transaction> retrieved = testTd.getTransactionsByCustomerId(2);
        Assert.assertEquals(expected, retrieved);
    }

    @Test
    public void when_invalid_customer_id_then_get_transactions_by_customer_id_returns_empty_list() {
        List<Transaction> expected = new ArrayList<>();
        List<Transaction> retrieved = testTd.getTransactionsByCustomerId(0);
        Assert.assertEquals("Invalid customer is should return empty list of transactions", expected, retrieved);
    }

    @Test
    public void when_valid_account_number_then_get_transactions_by_account_number_returns_transactions() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_1);
        List<Transaction> retrieved = testTd.getTransactionsByAccountNumber(1);
        Assert.assertEquals("Valid account number should return transactions", expected, retrieved);

    }

    @Test
    public void when_invalid_account_number_then_get_transactions_by_account_number_returns_empty_list() {
        List<Transaction> expected = new ArrayList<>();
        List<Transaction> retrieved = testTd.getTransactionsByAccountNumber(0);
        Assert.assertEquals("Valid account number should return transactions", expected, retrieved);

    }

    @Test
    public void when_account_number_and_amount_both_0_then_generate_report_returns_transactions_from_all_accounts() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_1);
        expected.add(TRANSACTION_5);
        expected.add(TRANSACTION_2);
        expected.add(TRANSACTION_3);
        List<Transaction> actual = testTd.generateReport(Timestamp.valueOf("2000-01-01 00:00:00"), Timestamp.valueOf("2023-12-01 00:00:00"), 0, BigDecimal.ZERO);
        Assert.assertEquals("Account number and balance of 0 should return all transactions", expected, actual);
    }

    @Test
    public void when_account_number_given_and_amount_0_then_generate_report_returns_transactions_for_account() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_1);
        List<Transaction> actual = testTd.generateReport(Timestamp.valueOf("2000-01-01 00:00:00"), Timestamp.valueOf("2023-12-01 00:00:00"), 1, BigDecimal.ZERO);
        Assert.assertEquals("Balance of 0 and specified account should return all transactions for account", expected, actual);

    }
    @Test
    public void when_account_number_given_and_amount_not_0_then_generate_report_returns_transactions_for_account_and_amount() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_5);
        List<Transaction> actual = testTd.generateReport(Timestamp.valueOf("2000-01-01 00:00:00"), Timestamp.valueOf("2023-12-01 00:00:00"), 2, new BigDecimal("200.0"));
        Assert.assertEquals("Account number and balance of 0 should return all transactions", expected, actual);
    }
    @Test
    public void when_account_number_given_and_amount_not_found_then_generate_report_returns_an_empty_list() {
        List<Transaction> expected = new ArrayList<>();
        List<Transaction> actual = testTd.generateReport(Timestamp.valueOf("2000-01-01 00:00:00"), Timestamp.valueOf("2023-12-01 00:00:00"), 2, new BigDecimal("67.0"));
        Assert.assertEquals("Account number and balance of 0 should return all transactions", expected, actual);
    }
    @Test
    public void when_account_number_0_and_amount_not_0_then_generate_report_returns_transactions_for_all_accounts_with_given_amount() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_5);
        expected.add(TRANSACTION_3);
        List<Transaction> actual = testTd.generateReport(Timestamp.valueOf("2000-01-01 00:00:00"), Timestamp.valueOf("2023-12-01 00:00:00"), 0, new BigDecimal("200.0"));
        Assert.assertEquals("Account number and balance of 0 should return all transactions", expected, actual);
    }
}

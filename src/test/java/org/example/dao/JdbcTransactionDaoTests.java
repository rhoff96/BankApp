package org.example.dao;

import org.example.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JdbcTransactionDaoTests extends BaseDaoTests {

    private JdbcTransactionDao testTd;
    private static final Transaction TRANSACTION_1 = new Transaction(LocalDateTime.of(2023,10,25,05,00,00), new BigDecimal("100"), 1, 1, 1, new BigDecimal("10"));
    private static final Transaction TRANSACTION_2 = new Transaction(LocalDateTime.of(2023, 11, 20,12,00,00), new BigDecimal("50"), 1, 1, 1, new BigDecimal("200"));

    @Before
    public void setup() {
        testTd = new JdbcTransactionDao(dataSource);
    }

    @Test
    public void when_valid_id_then_get_transaction() {
        Transaction retrieved = testTd.getTransactionById(1);
        Assert.assertEquals("Valid id should return transaction", TRANSACTION_1, retrieved);
    }
}

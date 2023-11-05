package org.example.dao;

import org.example.Logger;
import org.example.model.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LoggerTests {
    @Test
    public void when_write_csv_then_write_file_creates_correct_file(){
        Logger logger = new Logger();
        List<Transaction> transactionsTest = new ArrayList<>();
        Transaction test = new Transaction(Timestamp.valueOf("2023-11-05 00:00:00"),
                new BigDecimal("100.0"), 1,15, new BigDecimal("5.0"));
        transactionsTest.add(test);
        logger.writeFile(transactionsTest,"test",".csv");
        List<Transaction> retrieved = logger.readFile("test.csv");
        Assert.assertEquals("Files should match",transactionsTest,retrieved);

    }

}

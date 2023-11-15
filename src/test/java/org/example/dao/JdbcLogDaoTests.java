package org.example.dao;

import org.example.model.MonthlyLogEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class JdbcLogDaoTests extends BaseDaoTests{

    private JdbcLogDao testLd;

    @Before
    public void setup(){
        testLd = new JdbcLogDao(dataSource);
    }

    @Test
    public void when_get_last_logged_time_then_get_time(){
        MonthlyLogEntry actual = testLd.getLastLoggedTime("interest");
        MonthlyLogEntry expected = new MonthlyLogEntry(Timestamp.valueOf("2023-01-01 00:00:00"),"interest");
        Assert.assertEquals("Entries should be the same",expected,actual);
    }
}

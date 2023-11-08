package org.example.dao;

import org.example.CLI.UserInterface;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class UserInterfaceTests {
    UserInterface ui = new UserInterface();

    @Test
    public void when_long_input_then_round_big_decimal_rounds_correctly(){
        BigDecimal expected = new BigDecimal("10.06");
        BigDecimal actual = ui.roundBigDec(new BigDecimal("10.069876543"));
        Assert.assertEquals("Decimal should be rounded down to the hundreths place",expected,actual);

        expected = new BigDecimal("20.05");
        actual = ui.roundBigDec(new BigDecimal("20.055"));
        Assert.assertEquals("Decimal should be rounded down to the hundreths place",expected,actual);
    }
}

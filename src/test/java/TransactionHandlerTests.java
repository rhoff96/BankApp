import org.example.TransactionHandler;
import org.example.dao.*;
import org.example.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransactionHandlerTests extends BaseDaoTests {

    private AccountDao testAd;

    private TransactionDao testTd;

    private static final Account ACCOUNT_1 = new Account(1, 1, "Checking");



    @Before
    public void setup() {
        testAd = new JdbcAccountDao(dataSource);
        testTd = new JdbcTransactionDao(dataSource);
    }

    @Test
    public void when_checking_deposit_then_correct_transaction() {
        TransactionHandler handler = new TransactionHandler(1, new BigDecimal("35.00"), testAd, testTd);
        handler.processTransaction();
        BigDecimal expected = new BigDecimal("45.00");
        BigDecimal actual = testAd.getAccountById(1).getAccountBalance();
        Assert.assertEquals("Handler should deposit correct amount to checking", expected, actual);
    }

    @Test
    public void when_checking_withdraw_with_available_funds_then_correct_transaction() {
        TransactionHandler handler = new TransactionHandler(1, new BigDecimal("5.00"), testAd, testTd);
        handler.withdrawCheckingOrSavings();
        BigDecimal expected = new BigDecimal("5.00");
        BigDecimal actual = testAd.getAccountById(1).getAccountBalance();
        Assert.assertEquals("Handler should withdraw correct amount from checking", expected, actual);
    }

    @Test
    public void when_checking_withdraw_more_than_balance_return_0_balance() {
        TransactionHandler handler = new TransactionHandler(1, new BigDecimal("20.00"), testAd, testTd);
        handler.withdrawCheckingOrSavings();
        BigDecimal expected = new BigDecimal("0.0");
        BigDecimal actual = testAd.getAccountById(1).getAccountBalance();
        Assert.assertEquals("Handler should withdraw correct amount from checking", expected, actual);

    }

    @Test
    public void when_savings_deposit_then_return_correct_balance() {
        TransactionHandler handler = new TransactionHandler(2, new BigDecimal("20.00"), testAd, testTd);
        handler.processTransaction();
        BigDecimal expected = new BigDecimal("720.00");
        BigDecimal actual = testAd.getAccountById(2).getAccountBalance();
        Assert.assertEquals("Handler should deposit correct amount to savings", expected, actual);
    }

    @Test
    public void when_savings_withdraw_above_minimum_then_correct_balance() {
        TransactionHandler handler = new TransactionHandler(2, new BigDecimal("20.00"), testAd, testTd);
        handler.withdrawCheckingOrSavings();
        BigDecimal expected = new BigDecimal("680.00");
        BigDecimal actual = testAd.getAccountById(2).getAccountBalance();
        Assert.assertEquals("Handler should deposit correct amount to savings", expected, actual);

    }
    @Test
    public void when_savings_withdraw_below_minimum_then_overdraft_fee_applied(){
        TransactionHandler handler = new TransactionHandler(2, new BigDecimal("600.00"), testAd, testTd);
        handler.withdrawCheckingOrSavings();
        BigDecimal expected = new BigDecimal("90.00");
        BigDecimal actual = testAd.getAccountById(2).getAccountBalance();
        Assert.assertEquals("Handler should apply fee when withdrawal goes below minimum balance", expected, actual);

    }
    @Test
    public void when_savings_withdraw_more_than_balance_then_failure(){
        TransactionHandler handler = new TransactionHandler(2, new BigDecimal("800.00"), testAd, testTd);
        handler.withdrawCheckingOrSavings();
        BigDecimal expected = new BigDecimal("700.0");
        BigDecimal actual = testAd.getAccountById(2).getAccountBalance();
        Assert.assertEquals("Handler deny withdrawals greater than balance", expected, actual);
    }
    @Test
    public void when_transfer_then_handler_processes_correct_transactions(){
        TransactionHandler handler = new TransactionHandler(2, new BigDecimal("45.00"), testAd, testTd);
        handler.transfer(ACCOUNT_1);
        BigDecimal expectedSenderBalance = new BigDecimal("655.00");
        BigDecimal actualSenderBalance = testAd.getAccountById(2).getAccountBalance();
        Assert.assertEquals("Handler deny withdrawals greater than balance", expectedSenderBalance, actualSenderBalance);

        BigDecimal expectedReceiverBalance = new BigDecimal("55.00");
        BigDecimal actualReceiverBalance = testAd.getAccountById(ACCOUNT_1.getAccountNumber()).getAccountBalance();
        Assert.assertEquals("Handler deny withdrawals greater than balance", expectedReceiverBalance, actualReceiverBalance);


    }
}

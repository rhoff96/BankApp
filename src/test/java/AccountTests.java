import org.example.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTests {
    private final UserInterface ui = new UserInterface();
    private final User currentUser = new User("Russell","admin",ui);
    private final CheckingAccount acc = new CheckingAccount("Checking", 123, ui);
    private final SavingsAccount sa = new SavingsAccount("Savings", 123, ui);


    @Test
    public void when_checking_withdraw_greater_than_balance_then_return_0() {
        currentUser.userAccounts.add(acc);
        acc.balance = new BigDecimal("150");
        BigDecimal bd = new BigDecimal("200");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal actual = acc.withdraw(currentUser, acc, bd);
        Assert.assertEquals("Withdraw greater than current balance should return 0 balance", expected, actual);
    }
    @Test
    public void when_withdraw_negative_then_error_trans_fails(){
        acc.balance = new BigDecimal("250");
        BigDecimal bd = new BigDecimal("-10");
        BigDecimal expected = new BigDecimal("250");
        BigDecimal actual = acc.withdraw(currentUser, acc, bd);
        Assert.assertEquals("Negative withdrawals are not allowed.", expected,actual);
    }
    @Test
    public void when_withdraw_on_savings_below_min_balance_apply_fee() {
        sa.balance = new BigDecimal("150");
        BigDecimal bd = new BigDecimal("75");
        BigDecimal expected = new BigDecimal("65");
        BigDecimal actual = sa.withdraw(currentUser, acc, bd);
        Assert.assertEquals("Withdraw from savings below minimum balance incurs $10 fee", expected, actual);
    }
   // @Test
//    public void when_withdraw_more_than_2_times_withdraw_fail(){
//        //how to test multiple calls to method?
//        sa.balance = new BigDecimal("200");
//        sa.withdraw(currentUser, new BigDecimal("30"));
//        sa.withdraw(currentUser, new BigDecimal("20"));
//        sa.withdraw(currentUser, new BigDecimal("10"));
//        BigDecimal expected = new BigDecimal("150");
//        BigDecimal actual = sa.withdraw(sa.withdraw(sa.withdraw(currentUser, new BigDecimal("30"))));
//        Assert.assertEquals("More than two withdrawals from a savings account per session are not allowed", expected,actual);
//    }

    @Test
    public void when_savings_withdrawal_would_equal_min_then_no_fee() {
        sa.balance = new BigDecimal("150");
        BigDecimal bd = new BigDecimal("50");
        BigDecimal expected = new BigDecimal("100");
        BigDecimal actual = sa.withdraw(currentUser, currentUser.currentAccount, bd);
        Assert.assertEquals("Withdraw from savings resulting in minimum balance should not incur a $10 fee", expected, actual);
    }

    @Test
    public void when_savings_withdrawal_greater_than_balance_return_fail() {
        sa.balance = new BigDecimal("150");
        BigDecimal bd = new BigDecimal("200");
        BigDecimal expected = sa.balance;
        BigDecimal actual = sa.withdraw(currentUser, currentUser.currentAccount, bd);
        Assert.assertEquals("Withdraw from savings greater than current balance should fail and return current balance", expected, actual);
    }

    @Test
    public void when_0_balance_then_deposit_return_new_balance() {
        BigDecimal bd = new BigDecimal("25");
        BigDecimal actual = acc.deposit(currentUser, bd);
        BigDecimal expected = new BigDecimal("25");
        Assert.assertEquals("Deposit to empty account should return correct addition", expected, actual);
    }

    @Test
    public void when_checking_deposit_then_return_correct_addition() {
        acc.balance = new BigDecimal("50");
        BigDecimal bd = new BigDecimal("25");
        BigDecimal expected = new BigDecimal("75");
        BigDecimal actual = acc.deposit(currentUser, bd);
        Assert.assertEquals("Deposit to empty account should return correct addition", expected, actual);
    }
    @Test
    public void when_deposit_negative_return_transaction_fail(){
        acc.balance = new BigDecimal("200");
        BigDecimal bd = new BigDecimal("-20");
        BigDecimal expected = new BigDecimal("200");
        BigDecimal actual = acc.deposit(currentUser, bd);
        Assert.assertEquals("Negative deposits are not allowed.", expected,actual);
    }

    @Test
    public void when_transfer_less_than_balance_return_correct_amounts() {

    }
}

import org.example.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTests {
    private final UserInterface ui = new UserInterface();
    private final User currentUser = new User("TestUser", "admin", ui);
    private final CheckingAccount acc = new CheckingAccount("Checking", 123, ui);
    private final SavingsAccount sa = new SavingsAccount("Savings", 123, ui);


    @Test
    public void when_checking_withdraw_greater_than_balance_then_return_0() {
        currentUser.currentAccount = acc;
        currentUser.userAccounts.add(acc);
        acc.setBalance(new BigDecimal("150"));
        BigDecimal bd = new BigDecimal("200");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal actual = acc.withdraw(bd);
        Assert.assertEquals("Withdraw greater than current balance should return 0 balance", expected, actual);
    }

    @Test
    public void when_withdraw_on_savings_below_min_balance_apply_fee() {
        currentUser.currentAccount = sa;
        currentUser.userAccounts.add(sa);
        sa.setBalance(new BigDecimal("150"));
        BigDecimal bd = new BigDecimal("75");
        BigDecimal expected = new BigDecimal("65");
        BigDecimal actual = sa.withdraw(bd);
        Assert.assertEquals("Withdraw from savings below minimum balance incurs $10 fee", expected, actual);
    }

    @Test
    public void when_withdraw_more_than_2_times_withdraw_fail() {
        currentUser.currentAccount = sa;
        currentUser.userAccounts.add(sa);
        sa.setBalance(new BigDecimal("200"));
        sa.withdraw(new BigDecimal("30"));
        sa.withdraw(new BigDecimal("20"));
        sa.withdraw(new BigDecimal("10"));
        BigDecimal expected = new BigDecimal("150");
        BigDecimal actual = (sa.withdraw(new BigDecimal("30")));
        Assert.assertEquals("More than two withdrawals from a savings account per session are not allowed", expected, actual);
    }

    @Test
    public void when_savings_withdrawal_would_equal_min_then_no_fee() {
        currentUser.currentAccount = sa;
        currentUser.userAccounts.add(sa);
        sa.setBalance(new BigDecimal("150"));
        BigDecimal bd = new BigDecimal("50");
        BigDecimal expected = new BigDecimal("100");
        BigDecimal actual = sa.withdraw(bd);
        Assert.assertEquals("Withdraw from savings resulting in minimum balance should not incur a $10 fee", expected, actual);
    }

    @Test
    public void when_savings_withdrawal_greater_than_balance_return_fail() {
        currentUser.currentAccount = sa;
        currentUser.userAccounts.add(sa);

        sa.setBalance(new BigDecimal("150"));
        BigDecimal bd = new BigDecimal("200");
        BigDecimal expected = sa.getBalance();
        BigDecimal actual = sa.withdraw(bd);
        Assert.assertEquals("Withdraw from savings greater than current balance should fail and return current balance", expected, actual);
    }

    @Test
    public void when_0_balance_then_deposit_return_new_balance() {
        currentUser.currentAccount = acc;
        currentUser.userAccounts.add(acc);
        BigDecimal bd = new BigDecimal("25");
        BigDecimal actual = acc.deposit(bd);
        BigDecimal expected = new BigDecimal("25");
        Assert.assertEquals("Deposit to empty account should return correct addition", expected, actual);
    }

    @Test
    public void when_checking_deposit_then_return_correct_addition() {
        currentUser.currentAccount = acc;
        currentUser.userAccounts.add(acc);
        acc.setBalance(new BigDecimal("50"));
        BigDecimal bd = new BigDecimal("25");
        BigDecimal expected = new BigDecimal("75");
        BigDecimal actual = acc.deposit(bd);
        Assert.assertEquals("Deposit to empty account should return correct addition", expected, actual);
    }

    @Test
    public void when_transfer_return_correct_amounts() {
        currentUser.currentAccount = acc;
        currentUser.userAccounts.add(acc);
        currentUser.userAccounts.add(sa);
        acc.setBalance(new BigDecimal("500"));
        sa.setBalance(new BigDecimal("150"));
        acc.transfer(currentUser,sa,new BigDecimal("25"));
        BigDecimal actualChecking = acc.getBalance();
        BigDecimal expectedChecking = new BigDecimal("475");
        BigDecimal actualSavings = sa.getBalance();
        BigDecimal expectedSavings = new BigDecimal("175");
        Assert.assertEquals("Checking balance should be correct",expectedChecking,actualChecking);
        Assert.assertEquals("Savings balance should be correct", expectedSavings,actualSavings);

    }
    @Test
    public void when_transfer_more_than_account_balance_return(){
        currentUser.currentAccount = acc;
        currentUser.userAccounts.add(acc);
        currentUser.userAccounts.add(sa);
        acc.setBalance(new BigDecimal("200"));
        sa.setBalance(new BigDecimal("150"));
        acc.transfer(currentUser,sa,new BigDecimal("300"));
        BigDecimal actualChecking = acc.getBalance();
        BigDecimal expectedChecking = new BigDecimal("200");
        BigDecimal actualSavings = sa.getBalance();
        BigDecimal expectedSavings = new BigDecimal("150");
        Assert.assertEquals("Checking balance should be correct",expectedChecking,actualChecking);
        Assert.assertEquals("Savings balance should be correct", expectedSavings,actualSavings);

    }
}

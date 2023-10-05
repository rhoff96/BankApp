package org.example;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private final BigDecimal MINIMUM_BALANCE = new BigDecimal(100);
    private final BigDecimal OVERDRAFT_FEE = new BigDecimal(10);
    private int withdrawalCounter = 0;

    public SavingsAccount(String accountType, int accountNumber, UserInterface ui) {
        super(accountType, accountNumber, ui);
    }
    public void accrueInterest(User currentUser){
        //work in progress
    }

    @Override
    public BigDecimal withdraw(BigDecimal bd) {
        if (bd.compareTo(BigDecimal.ZERO) < 0){
            System.out.println("Please enter a positive withdrawal amount");
            return this.getBalance();
        }
        final int SAVINGS_WITHDRAWAL_MAX = 2;
        if (withdrawalCounter == SAVINGS_WITHDRAWAL_MAX) {
            System.out.println("You have reached the maximum number of allowed withdrawals per session from a savings account.");
            return this.getBalance();
        }

        if (this.getBalance().subtract(bd).compareTo(MINIMUM_BALANCE) < 0) {
            if (this.getBalance().subtract(bd).compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Withdrawals equal or greater than current balance are not allowed. Transaction failed");
                return this.getBalance();
            } else {
                this.balance = this.balance.subtract(bd.add(OVERDRAFT_FEE));
                withdrawalCounter++;
                System.out.println("Your balance fell below the minimum balance of $100 and is assessed a fee of $10.");
                System.out.println("Your current balance is $" + this.getBalance());
                System.out.printf("You have %d remaining withdrawals today. ", SAVINGS_WITHDRAWAL_MAX - withdrawalCounter);
            }
        } else {
            super.withdraw(bd);
            withdrawalCounter++;
            System.out.printf("You have %d remaining withdrawals today. ", SAVINGS_WITHDRAWAL_MAX - withdrawalCounter);

        }
        return this.getBalance();
    }
}

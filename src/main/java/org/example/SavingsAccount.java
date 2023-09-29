package org.example;

import java.math.BigDecimal;
import java.util.Scanner;

public class SavingsAccount extends Account {
    Scanner userInput = new Scanner(System.in);
    private final BigDecimal MINIMUM_BALANCE = new BigDecimal(100);
    private final BigDecimal OVERDRAFT_FEE = new BigDecimal(10);
    private UserInterface ui;

    public SavingsAccount(String accountType, int accountNumber, UserInterface ui) {
        super(accountType, accountNumber, ui);
    }

    @Override
    public void withdraw() {
        ui.put("Please enter withdrawal amount: ");
        BigDecimal bigWithdrawal = ui.getBigDec();
        if (this.getBalance().subtract(bigWithdrawal).compareTo(MINIMUM_BALANCE) < 0) {
            this.balance.subtract(bigWithdrawal.add(OVERDRAFT_FEE));
            System.out.println("Your balance fell below the minimum balance of $100 and is assessed a fee of $10.");
            System.out.println("Your current balance is $" + this.balance);
        } else {
            super.withdraw();
        }
    }
}

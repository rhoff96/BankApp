package org.example;

import java.math.BigDecimal;
import java.util.Scanner;

public class Transaction {
    Scanner userInput = new Scanner(System.in);
    public User currentUser;
    public Account currentAccount;
    private boolean isBanking = true;
    private int withdrawalCounter = 0;
    private int SAVINGS_WITHDRAWAL_MAX = 2;
    private UserInterface ui;

    public Transaction(User currentUser, Account currentAccount, UserInterface ui) {
        this.currentAccount = currentAccount;
        this.currentUser = currentUser;
        this.ui = ui;
    }


    public void transact() {

        while (isBanking) {

            if (currentAccount.getBalance().compareTo(new BigDecimal(0)) == 0) {
                System.out.printf("Your %s account has a balance of $0.\n", currentAccount.accountType);
                currentAccount.deposit();
            }
            if (currentUser.userAccounts.size() == 1) {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, (T)ransfer between accounts, (L)ist accounts, or (O)pen another account? ");
            }
            String choice = ui.getAlpha();

            transactionAction(choice);

            boolean wrongChoice = true;
            while (wrongChoice) {
                ui.put("Current account is " + currentUser.currentAccount.accountType + " #" + currentUser.currentAccount.getAccountNumber());
                ui.put("Would you like to keep banking? (y/n)");
                String response = ui.getAlpha();
                if (response.equalsIgnoreCase("n")) {
                    wrongChoice = false;
                    isBanking = false;
                } else if (response.equalsIgnoreCase("y")) {
                    wrongChoice = false;
                    isBanking = true;
                } else {
                   ui.put("Please enter y or n.");
                }
            }
        }
        ui.put("Banking session complete.");
    }

    public void transactionAction(String choice) {
        if (choice.equalsIgnoreCase("W")) {
            if (currentAccount.accountType.equals("Savings")) {
                if (withdrawalCounter == SAVINGS_WITHDRAWAL_MAX) {
                    ui.put("You have reached the maximum number of allowed withdrawals per session in a savings account.");
                } else {
                    currentAccount.withdraw();
                    withdrawalCounter++;
                    System.out.printf("You have %d remaining withdrawals today. ", SAVINGS_WITHDRAWAL_MAX - withdrawalCounter);
                }
            } else {
                currentAccount.withdraw();
            }
        }
        if (choice.equalsIgnoreCase("D")) {
            currentAccount.deposit();
        }
        if (choice.equalsIgnoreCase("G")) {
            ui.put("Your current balance is $" + currentAccount.getBalance());
        }
        if (choice.equalsIgnoreCase("T")) {
            currentAccount.transfer(currentUser);
        }
        if (choice.equalsIgnoreCase("O")) {
            currentAccount = currentUser.createAccount(currentUser);
        }
        if (choice.equalsIgnoreCase("L")) {
           ui.put(currentUser.getTotalBalance(currentUser));
        }
    }
}



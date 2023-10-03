package org.example;

import java.math.BigDecimal;

public class Transaction {
    public User currentUser;
    public Account currentAccount;
    private boolean isBanking = true;
    private final UserInterface ui;

    public Transaction(User currentUser, Account currentAccount, UserInterface ui) {
        this.currentAccount = currentAccount;
        this.currentUser = currentUser;
        this.ui = ui;
    }


    public void transact() {

        while (isBanking) {

            if (currentAccount.getBalance().compareTo(new BigDecimal(0)) == 0) {
                System.out.printf("Your %s account has a balance of $0.\n", currentAccount.accountType);
                ui.put("Please enter deposit amount: ");
                BigDecimal bigCredit = ui.getBigDec();
                currentAccount.deposit(bigCredit);
            }
            if (currentUser.userAccounts.size() == 1) {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (T)ransfer between accounts," +
                        " (L)ist accounts, (S)witch accounts or (O)pen another account? ");
            }
            String choice = ui.getAlpha();

            transactionAction(choice);

            boolean wrongChoice = true;
            while (wrongChoice) {
                ui.put("Current account is " + currentUser.currentAccount.toString());
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
            ui.put("Please enter withdrawal amount: ");
            BigDecimal bigDebit = ui.getBigDec();
            currentAccount.withdraw(bigDebit);
        }
        if (choice.equalsIgnoreCase("D")) {
            ui.put("Please enter deposit amount: ");
            BigDecimal bigCredit = ui.getBigDec();
            currentAccount.deposit(bigCredit);
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
        if (choice.equalsIgnoreCase("S")) {
            currentUser.selectAccount(currentUser);
        }
    }
}



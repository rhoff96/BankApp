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
                currentAccount.deposit(currentUser, bigCredit);
            }
            if (currentUser.userAccounts.size() == 1) {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (T)ransfer between accounts," +
                        " (L)ist accounts, (S)witch accounts or (O)pen another account? ");
            }
            String choice = ui.getAlpha().toLowerCase();

            transactionAction(choice);

            while (true) {
                ui.put("Current account is " + currentUser.currentAccount.toString());
                ui.put("Would you like to keep banking? (y/n)");
                String response = ui.getAlpha();
                if (response.equalsIgnoreCase("n")) {
                    isBanking = false;
                    break;
                } else if (response.equalsIgnoreCase("y")) {
                    break;
                } else {
                    ui.put("Please enter y or n.");
                }
            }
        }
        currentUser.setTier(currentUser);
        ui.put("Banking session complete.");
    }

    public void transactionAction(String choice) {
        switch (choice) {
            case "w":
                ui.put("Please enter withdrawal amount: ");
                BigDecimal bigDebit = ui.getBigDec();
                currentAccount.withdraw(currentUser, currentAccount, bigDebit);
                break;
            case "d":
                ui.put("Please enter deposit amount: ");
                BigDecimal bigCredit = ui.getBigDec();
                currentAccount.deposit(currentUser, bigCredit);
                break;
            case "g":
                ui.put("Your current balance is $" + currentAccount.getBalance());
                break;
            case "t":
                currentAccount.transfer(currentUser);
                break;
            case "o":
                currentAccount = currentUser.createAccount(currentUser);
                break;
            case "l":
                ui.put("Total Balance: $ " + currentUser.getTotalBalance(currentUser));
                break;
            case "s":
                currentUser.selectAccount(currentUser);
                break;
            default:
                ui.put("Please select an option from the list.");
                transact();
        }
    }
}



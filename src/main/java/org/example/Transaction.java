package org.example;

import java.math.BigDecimal;

public class Transaction {
    public User currentUser;
    public Account currentAccount;
    private boolean isBanking = true;
    private final UserInterface ui;
    private final Log log = new Log();


    public Transaction(User currentUser, Account currentAccount, UserInterface ui) {
        this.currentAccount = currentAccount;
        this.currentUser = currentUser;
        this.ui = ui;
    }


    public void transact() {
        while (isBanking) {
            if (currentAccount.getBalance().compareTo(new BigDecimal(0)) == 0) {
                System.out.printf("Your %s account has a balance of $0.\n", currentAccount.getAccountType());
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
            String choice = ui.getAlpha().toLowerCase();

            transactionAction(choice);
            keepBanking();
        }
        currentUser.setTier(currentUser);
        ui.put("Banking session complete.");
    }

    public void keepBanking() {
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

    public void transactionAction(String choice) {
        String typeAmount = "";
        switch (choice) {
            case "w":
                ui.put("Please enter withdrawal amount: ");
                BigDecimal bigDebit = ui.getBigDec();
                BigDecimal tempBalance = currentAccount.getBalance();
                currentAccount.withdraw(bigDebit);
                BigDecimal withdrawal = tempBalance.subtract(currentAccount.getBalance());
                typeAmount = "Withdraw $" + withdrawal;
                break;
            case "d":
                ui.put("Please enter deposit amount: ");
                BigDecimal bigCredit = ui.getBigDec();
                currentAccount.deposit(bigCredit);
                typeAmount = "Deposit $" + bigCredit;
                break;
            case "g":
                ui.put("Your current balance is $" + currentAccount.getBalance());
                break;
            case "t":
                ui.put("Please provide transfer amount: ");
                BigDecimal transferBig = ui.getBigDec();
                if (transferBig.compareTo(currentUser.currentAccount.getBalance()) > 0) {
                    ui.put("Amount must be less than current balance.");
                    return;
                }
                Account accountTo = currentAccount.pickTransferAccount(currentUser);
                boolean enoughFunds = currentAccount.transfer(currentUser, accountTo, transferBig);
                if (!enoughFunds) return;
                ui.put("Transfer completed");
                typeAmount = "Transfer $" + transferBig;
                log.logEntry(currentUser,accountTo,typeAmount);
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
        log.logEntry(currentUser, currentUser.currentAccount, typeAmount);
    }
}



package org.example;

import java.math.BigDecimal;

public abstract class Account {
    public String accountType;
    private final int accountNumber;
    public BigDecimal balance;
    private final UserInterface ui;
    public Log log = new Log();


    public Account(String accountType, int accountNumber, UserInterface ui) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.ui = ui;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal withdraw(User currentUser, Account currentAccount, BigDecimal bd) {
        if (bd.compareTo(BigDecimal.ZERO) < 0) {
            ui.put("Please enter a positive withdrawal amount");
            return this.getBalance();
        }
        BigDecimal tempBalance = this.balance;
        this.balance = this.balance.subtract(bd);
        if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
            ui.put("Current balance is: $" + this.balance);
        } else {
            ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + tempBalance);
            this.balance = BigDecimal.ZERO;
            ui.put("Your current balance is $" + this.balance);
        }
        BigDecimal withdrawal = tempBalance.subtract(this.getBalance());
        String typeAmount = "Withdraw $"+withdrawal;
        log.logEntry(currentUser, currentUser.currentAccount, typeAmount);
        return this.getBalance();
    }

    public void transfer(User currentUser) {
        ui.put("Current account is #" + currentUser.currentAccount.getAccountNumber() + currentUser.currentAccount.getBalance());
        ui.put("Available accounts to transfer to: ");
        for (int i = 0; i < currentUser.userAccounts.size(); i++) {
            if (currentUser.userAccounts.get(i).accountNumber != currentUser.currentAccount.accountNumber)
                ui.put(currentUser.userAccounts.get(i).toString());
        }
        Account accountTo = null;
        ui.put("Please enter the account number to transfer to: ");
        int acctNumToTransferTo = ui.getInt();
        for (Account acct : currentUser.userAccounts) {
            if (acct.getAccountNumber() == acctNumToTransferTo) {
                accountTo = acct;
            }
        }
        if (accountTo == null) {
            System.out.println("Account not found\n");
            return;
        }
        ui.put("Please provide transfer amount: ");
        BigDecimal transferBig = ui.getBigDec();
        if (transferBig.compareTo(currentUser.currentAccount.balance) > 0) {
            ui.put("Amount must be less than current balance.");
            return;
        }
        currentUser.currentAccount.balance = currentUser.currentAccount.balance.subtract(transferBig);
        for (Account account : currentUser.userAccounts) {
            if (account.getAccountNumber() == acctNumToTransferTo) {
                account.balance = account.balance.add(transferBig);
            }
        }
        ui.put("Transfer completed.");
        currentUser.getTotalBalance(currentUser);
        String typeAmount = "Transfer $" + transferBig;
        log.logEntry(currentUser, currentUser.currentAccount, typeAmount);
    }

    public BigDecimal deposit(User currentUser, BigDecimal bd) {
        if (bd.compareTo(BigDecimal.ZERO) < 0) {
            ui.put("Please enter a positive deposit amount");
            return this.getBalance();
        }
        this.balance = balance.add(bd);
        System.out.println("Current balance is: $" + this.balance);
        String typeAmount = "Deposit $"+bd;
        log.logEntry(currentUser, currentUser.currentAccount, typeAmount);
        return this.balance;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return this.accountType + " #" + this.getAccountNumber();
    }
}

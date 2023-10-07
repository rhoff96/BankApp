package org.example;

import java.math.BigDecimal;

public abstract class Account {
    private final String accountType;
    private final int accountNumber;
    private BigDecimal balance;
    private final UserInterface ui;
    private final Log log = new Log();


    public Account(String accountType, int accountNumber, UserInterface ui) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.ui = ui;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal withdraw(User currentUser, Account currentAccount, BigDecimal bd) {
        if (bd.compareTo(BigDecimal.ZERO) < 0) {
            ui.put("Please enter a positive withdrawal amount");
            return this.getBalance();
        }
        BigDecimal tempBalance = this.getBalance();
        this.setBalance(this.balance.subtract(bd));
        if (this.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            ui.put("Current balance is: $" + this.getBalance());
        } else {
            ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + tempBalance);
            this.setBalance(BigDecimal.ZERO);
            ui.put("Your current balance is $" + this.getBalance());
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
        if (transferBig.compareTo(currentUser.currentAccount.getBalance()) > 0) {
            ui.put("Amount must be less than current balance.");
            return;
        }
        currentUser.currentAccount.setBalance(currentUser.currentAccount.getBalance().subtract(transferBig));
        for (Account account : currentUser.userAccounts) {
            if (account.getAccountNumber() == acctNumToTransferTo) {
                account.setBalance(account.getBalance().add(transferBig));
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
        this.setBalance(this.getBalance().add(bd));
        System.out.println("Current balance is: $" + this.getBalance());
        String typeAmount = "Deposit $"+bd;
        log.logEntry(currentUser, currentUser.currentAccount, typeAmount);
        return this.getBalance();
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return this.accountType + " #" + this.getAccountNumber();
    }
}

package org.example;

import java.math.BigDecimal;
import java.util.Scanner;

public abstract class Account {
    public String accountType;
    private int accountNumber;
    protected BigDecimal balance;
    private UserInterface ui;


    public Account(String accountType, int accountNumber, UserInterface ui) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.ui = ui;


    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void withdraw() {
        ui.put("Please enter withdrawal amount: ");
        BigDecimal tempBalance = balance;
        BigDecimal bigDebit = ui.getBigDec();
        this.balance = this.balance.subtract(bigDebit);
        if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
            ui.put("Current balance is: $" + this.balance);
        } else {
            ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + tempBalance);
            this.balance = BigDecimal.ZERO;
            ui.put("Your current balance is $" + this.balance);
        }
    }

    public void transfer(User currentUser) {
        ui.put("Available accounts for transfer: ");
        for (int i = 0; i < currentUser.userAccounts.size(); i++) {
            if (currentUser.userAccounts.get(i) != currentUser.currentAccount)
                ui.put(currentUser.userAccounts.get(i).accountType + "  #" + currentUser.userAccounts.get(i).getAccountNumber() + ": $" + currentUser.userAccounts.get(i).getBalance());
        }
        Account accountTo = null;
        ui.put("Please enter the account number to transfer to: ");
        String transferTo = ui.getAlpha();
        int acctNumToTransferTo = Integer.parseInt(transferTo);
//        if (!currentUser.userAccounts.contains(acctNumToTransferTo)) {
//            System.out.println("Account not found\n");
//            transfer(currentUser);
//        }
        ui.put("Please provide transfer amount: ");
        BigDecimal transferBig = ui.getBigDec();
        currentUser.currentAccount.balance = currentUser.currentAccount.balance.subtract(transferBig);
        for (Account account : currentUser.userAccounts) {
            if (account.getAccountNumber() == acctNumToTransferTo) {
                accountTo = account;
                account.balance = accountTo.balance.add(transferBig);
            }
        }
        ui.put("Transfer completed.");
        currentUser.getTotalBalance(currentUser);
        //System.out.println("Other account balance is: $" + accountTo.getBalance());
        //System.out.println("Your current account is: "+ currentUser.currentAccount.accountType + " #"+ currentUser.currentAccount.accountNumber);
    }

    public void deposit() {
        ui.put("Please enter deposit amount: ");
        BigDecimal bigCredit = ui.getBigDec();
        this.balance = balance.add(bigCredit);
        System.out.println("Current balance is: $" + this.balance);
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

}

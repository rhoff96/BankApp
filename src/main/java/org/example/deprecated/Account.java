package org.example.deprecated;

import java.math.BigDecimal;

//public class Account {
//    private int accountNumber;
//    private int customerId;
//    private final String accountType;
//    private BigDecimal balance;
//    private UserInterface ui;
//
//    public Account(String accountType, int customerId) {
//        this.accountType = accountType;
//        this.customerId = customerId;
//    }
//
//    public int getAccountNumber() {
//        return accountNumber;
//    }
//
//    public String getAccountType() {
//        return accountType;
//    }
//
//    public void setBalance(BigDecimal balance) {
//        this.balance = balance;
//    }
//
//    public BigDecimal deposit(BigDecimal bd) {
//        this.setBalance(this.getBalance().add(bd));
//        return this.getBalance();
//    }

//    public BigDecimal withdraw(BigDecimal bd) {
//        BigDecimal tempBalance = this.getBalance();
//        this.setBalance(this.balance.subtract(bd));
//        if (this.getBalance().compareTo(BigDecimal.ZERO) < 0) {
//            ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + tempBalance);
//            this.setBalance(BigDecimal.ZERO);
//        }
//        ui.put("Current balance is: $" + this.getBalance());
//        return this.getBalance();
//    }

//    public boolean transfer(User currentUser, Account accountTo, BigDecimal transferBig) {
//        if (currentUser.currentAccount.getBalance().compareTo(transferBig) < 0) {
//            return false;
//        } else {
//            ui.put("Transfer completed");
//            currentUser.currentAccount.withdraw(transferBig);
//            accountTo.deposit(transferBig);
//        }
//        return true;
//
//    }

//    public Account pickTransferAccount(User currentUser) {
//        ui.put("Current account is #" + currentUser.currentAccount.getAccountNumber() + " Balance $" + currentUser.currentAccount.getBalance());
//        ui.put("Available accounts to transfer to: ");
//        for (int i = 0; i < currentUser.userAccounts.size(); i++) {
//            if (currentUser.userAccounts.get(i).accountNumber != currentUser.currentAccount.accountNumber)
//                ui.put(currentUser.userAccounts.get(i).toString());
//        }
//        Account accountTo = null;
//        ui.put("Please enter the account number to transfer to: ");
//        int acctNumToTransferTo = ui.getInt();
//        for (Account acct : currentUser.userAccounts) {
//            if (acct.getAccountNumber() == acctNumToTransferTo) {
//                accountTo = acct;
//            }
//        }
//        if (accountTo == null) {
//            System.out.println("Account not found\n");
//            return null;
//        }
//        return accountTo;
//
//    }
//
//    public BigDecimal getBalance() {
//        return this.balance;
//    }

//    @Override
//    public String toString() {
//        return this.accountType + " #" + this.getAccountNumber();
//    }
//}

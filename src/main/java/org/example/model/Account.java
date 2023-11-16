package org.example.model;


import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Account {
    private int accountNumber;
    private int customerId;
    private String accountType;
    private BigDecimal accountBalance;
    private int withdrawalCount;
    private DecimalFormat df = new DecimalFormat("#,###.00");

    public int getWithdrawalCount() {
        return withdrawalCount;
    }

    public void setWithdrawalCount(int withdrawalCount) {
        this.withdrawalCount = withdrawalCount;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Account() {
    }
    public Account(int accountNumber, int customerId, String accountType){
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;

    }
    public Account(int accountNumber, int customerId, String accountType, int withdrawalCount) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.withdrawalCount = withdrawalCount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return this.accountType + " #" + this.getAccountNumber()
                + " Current Balance: $" + df.format(this.getAccountBalance());
    }

    @Override
    public boolean equals(Object obj) {
        Account other = (Account) obj;
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        if (!this.getAccountType().equals(other.getAccountType())) {
            return false;
        }
        if (!(this.getAccountNumber() == other.getAccountNumber())) {
            return false;
        }
        if (this.getAccountBalance().compareTo(other.getAccountBalance()) != 0) {
            return false;
        }
        return this.getCustomerId() == other.getCustomerId();
    }
}

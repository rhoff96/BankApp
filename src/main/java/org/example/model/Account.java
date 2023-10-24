package org.example.model;

import org.example.dao.AccountDao;
import org.example.dao.JdbcAccountDao;

public class Account {
    private int accountNumber;
    private int customerId;
    private String accountType;
    public Account() {
    }

    public Account(int customerId, String accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
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
}

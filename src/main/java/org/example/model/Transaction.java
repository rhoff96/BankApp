package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private LocalDate time;
    private int transactionId;
    private int customerId;
    private int accountNumber;
    private BigDecimal previousBalance;
    private BigDecimal amount;

    public Transaction(){}
    public Transaction(LocalDate time, int transactionId, int customerId, int accountNumber, BigDecimal amount) {
        this.time = time;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }
    public LocalDate getTime() {
        return time;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setTime(LocalDate time) {
        this.time = time;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }
}
package org.example.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private Timestamp time;
    private int transactionId;
    private int accountNumber;
    private BigDecimal previousBalance;
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(Timestamp time, BigDecimal previousBalance, int transactionId, int accountNumber, BigDecimal amount) {
        this.time = time;
        this.previousBalance = previousBalance;
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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

    @Override
    public boolean equals(Object obj) {
        Transaction other = (Transaction) obj;
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        if (!this.getAmount().equals(other.getAmount())) {
            return false;
        }
        if (!(this.getAccountNumber() == other.getAccountNumber())) {
            return false;
        }
        if (!(this.getPreviousBalance().equals(other.getPreviousBalance()))) {
            return false;
        }
        return this.getTransactionId() == other.getTransactionId();

    }

//    @Override
//    public String toString() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return this.getTime().toLocalDateTime().format(dtf) + " Account #"+ this.getAccountNumber() + " $"+ this.getAmount();
//
//    }
}

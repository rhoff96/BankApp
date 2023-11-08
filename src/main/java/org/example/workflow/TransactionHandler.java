package org.example.workflow;

import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.model.Account;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

public class TransactionHandler {
    private int accountNumber;
    private BigDecimal amount;
    private final AccountDao ad;
    private final TransactionDao td;

    public TransactionHandler(int accountNumber, BigDecimal amount, AccountDao ad, TransactionDao td) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.ad = ad;
        this.td = td;
    }

    public Transaction processTransaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setPreviousBalance(ad.getAccountById(accountNumber).getAccountBalance());
        newTransaction.setTime(Timestamp.from(Instant.now()));
        newTransaction.setAmount(amount);
        newTransaction.setAccountNumber(accountNumber);
        return td.createTransaction(newTransaction);
    }

    public boolean transfer(Account accountTo) {
        BigDecimal balance = ad.getAccountById(accountNumber).getAccountBalance();
        if (ad.getAccountById(accountNumber).getAccountType().equals("Savings")) {
            savingsWithdraw();
            amount = amount.multiply(BigDecimal.valueOf(-1));
            if (balance.compareTo(ad.getAccountById(accountNumber).getAccountBalance()) == 0){
                return false;
            } else {
                accountNumber = accountTo.getAccountNumber();
                processTransaction();
                return true;
            }
        } else {
            int tempAcctNumber = accountNumber;
            accountNumber = accountTo.getAccountNumber();
            processTransaction();
            accountNumber = tempAcctNumber;
            amount = amount.multiply(BigDecimal.valueOf(-1));
            processTransaction();
        }
        return false;
    }

    public BigDecimal withdrawCheckingOrSavings() {
        if (ad.getAccountById(accountNumber).getAccountType().equals("Checking")) {
            return checkingWithdraw();
        } else return savingsWithdraw();
    }

    public BigDecimal checkingWithdraw() {
        BigDecimal tempBalance = ad.getAccountById(accountNumber).getAccountBalance();
        BigDecimal test = tempBalance.subtract(amount);
        if (test.compareTo(BigDecimal.ZERO) < 0) {
            amount = tempBalance.multiply(BigDecimal.valueOf(-1));
        } else {
            amount = (amount.multiply(BigDecimal.valueOf(-1)));
        }
        processTransaction();
        return ad.getAccountById(accountNumber).getAccountBalance();
    }

    public BigDecimal savingsWithdraw() {
        final int SAVINGS_WITHDRAWAL_MAX = 5;
        final BigDecimal MINIMUM_BALANCE = new BigDecimal("500.0");
        final BigDecimal OVERDRAFT_FEE = new BigDecimal("10.0");

        if (ad.getAccountById(accountNumber).getWithdrawalCount() == SAVINGS_WITHDRAWAL_MAX) {
            return new BigDecimal("-3.0");
        }
        if (ad.getAccountById(accountNumber).getAccountBalance().subtract(amount).compareTo(MINIMUM_BALANCE) < 0) {
            if (ad.getAccountById(accountNumber).getAccountBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0 ||
                    ad.getAccountById(accountNumber).getAccountBalance().subtract(amount).subtract(OVERDRAFT_FEE).compareTo(BigDecimal.ZERO) < 0) {
                return new BigDecimal("-1.0");
            } else {
                amount = (amount.add(OVERDRAFT_FEE)).multiply(BigDecimal.valueOf(-1));
                processTransaction();
                Account toUpdate = ad.getAccountById(accountNumber);
                int currentWithdrawalCount = toUpdate.getWithdrawalCount();
                toUpdate.setWithdrawalCount(currentWithdrawalCount + 1);
                ad.updateAccount(toUpdate);
                return new BigDecimal("-2.0");
            }
        }
        Account toUpdate = ad.getAccountById(accountNumber);
        int currentWithdrawalCount = toUpdate.getWithdrawalCount();
        toUpdate.setWithdrawalCount(currentWithdrawalCount + 1);
        ad.updateAccount(toUpdate);
        checkingWithdraw();

        return ad.getAccountById(accountNumber).getAccountBalance();
    }

}

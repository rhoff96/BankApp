package org.example.monthly;

import org.example.dao.AccountDao;
import org.example.dao.CustomerDao;
import org.example.dao.LogDao;
import org.example.dao.TransactionDao;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceTaskFactory {

    private final AccountDao accountDao;
    private final CustomerDao customerDao;
    private final TransactionDao transactionDao;
    private final LogDao logDao;

    public MaintenanceTaskFactory(AccountDao accountDao, CustomerDao customerDao, TransactionDao transactionDao, LogDao logDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
        this.logDao = logDao;

    }

    public boolean determineRun(String action) {
        Timestamp lastTime = logDao.getLastLoggedTime(action).getTime();
        LocalDateTime monthLater = lastTime.toLocalDateTime().plusDays(30);
        return Timestamp.valueOf(LocalDateTime.now()).compareTo(Timestamp.valueOf(monthLater)) >= 0;
    }


    public void chargeMaintenanceFee() {
                if (determineRun("fee")) {
                    List<Account> accounts = accountDao.getAllAccounts();
                    for (Account account : accounts) {
                        boolean belowMinimum = (account.getAccountBalance().compareTo(new BigDecimal("500.00")) < 0);
                        if (account.getAccountType().equals("Savings") && belowMinimum) {
                            Transaction transaction = new Transaction();
                            transaction.setTime(Timestamp.valueOf(LocalDateTime.now()));
                            transaction.setPreviousBalance(account.getAccountBalance());
                            transaction.setAccountNumber(account.getAccountNumber());
                            transaction.setAmount(new BigDecimal("-15.00"));

                            transactionDao.createTransaction(transaction);
                        } else if (account.getAccountType().equals("Savings")) {
                            account.setWithdrawalCount(0);
                        }
                    }
                    logDao.createLogEntry("fee");
                }
    }

    public void accrueInterest() {
        if (determineRun("interest")) {
            List<Account> accounts = accountDao.getAllAccounts();

            BigDecimal interestRate = null;

            for (Account account : accounts) {
                if (account.getAccountType().equals("Savings")) {
                    Customer customer = customerDao.getCustomerById(account.getCustomerId());
                    Customer.Tier tier = customer.getTier();
                    if (tier == Customer.Tier.Bronze) {
                        interestRate = new BigDecimal("0.02");
                    }
                    if (tier == Customer.Tier.Silver) {
                        interestRate = new BigDecimal("0.03");
                    }
                    if (tier == Customer.Tier.Gold) {
                        interestRate = new BigDecimal("0.04");
                    }
                    if (tier == Customer.Tier.Platinum) {
                        interestRate = new BigDecimal("0.05");
                    }

                    BigDecimal currentBalance = account.getAccountBalance().setScale(2, RoundingMode.DOWN);
                    assert interestRate != null;
                    BigDecimal monthlyRate = interestRate.divide(new BigDecimal("12.00"), 20, RoundingMode.DOWN);
                    BigDecimal interestAmount = currentBalance.multiply(monthlyRate);
                    BigDecimal roundedAmount = interestAmount.setScale(2, RoundingMode.DOWN);

                    Transaction transaction = new Transaction();
                    transaction.setTime(Timestamp.valueOf(LocalDateTime.now()));
                    transaction.setPreviousBalance(currentBalance);
                    transaction.setAccountNumber(account.getAccountNumber());
                    transaction.setAmount(roundedAmount);

                    transactionDao.createTransaction(transaction);
                }
            }
            logDao.createLogEntry("interest");
        }

    }
}

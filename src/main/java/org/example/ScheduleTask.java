package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.dao.JdbcAccountDao;
import org.example.dao.JdbcCustomerDao;
import org.example.dao.JdbcTransactionDao;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTask {

    private JdbcAccountDao accountDao;
    private JdbcCustomerDao customerDao;
    private JdbcTransactionDao transactionDao;

    public void setup() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/Bank");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        accountDao = new JdbcAccountDao(dataSource);
        customerDao = new JdbcCustomerDao(dataSource);
        transactionDao = new JdbcTransactionDao(dataSource);


    }

    public void chargeMaintenanceFee() {
        TimerTask repeatedTask = new TimerTask() {

            public void run() {
                setup();
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
                    }
                }
                System.out.println("Maintenance fee assessed on " + LocalDateTime.now());
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        long period = 1000L * 8L;
        //long period = (1000L * 60L * 60L * 24L * 365L)/12;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }


    public void accrueInterest() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                setup();
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
                    System.out.println("Interest accrued on " + LocalDate.now());
                }
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        long period = 1000L * 8L;
        //long period = (1000L * 60L * 60L * 24L * 365L)/12;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }
}

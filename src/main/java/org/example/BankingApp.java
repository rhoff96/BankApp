package org.example;


import org.apache.commons.dbcp2.BasicDataSource;
import org.example.dao.*;
import org.example.model.Account;
import org.example.model.Customer;

import javax.sql.DataSource;

public class BankingApp {
    private final CustomerDao customerDao;
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public void run() {
        UserInterface ui = new UserInterface();
        Session session = new Session(ui);
        Customer currentCustomer = session.welcome();
        boolean isNew = session.userIsNew;
        String accountType = session.promptForAccountType();
        Account currentAccount = session.returnAccount(isNew, accountType);
//        Transaction transact = new Transaction(currentCustomer, currentAccount, ui);
//        transact.transact();
    }

    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/Bank");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        BankingApp ba = new BankingApp(dataSource);
        ba.run();
    }

    public BankingApp(DataSource dataSource) {
        customerDao = new JdbcCustomerDao();
        accountDao = new JdbcAccountDao();
        transactionDao = new JdbcTransactionDao();
    }
}



package org.example;


import org.apache.commons.dbcp2.BasicDataSource;
import org.example.dao.*;
import org.example.model.Account;
import org.example.model.Customer;

import javax.sql.DataSource;

public class BankingApp {
//    private final CustomerDao customerDao;
//    private final AccountDao accountDao;
//    private final TransactionDao transactionDao;

    public void run() {
        UserInterface ui = new UserInterface();
        Session session = new Session(ui);
        session.setup();
        String accountType = null;
        if (session.customerIsNew) {
            accountType = session.promptForAccountType();
        }
        session.createOrSelectAccount(session.customerIsNew, accountType);
        session.transact();
    }

    public static void main(String[] args) {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/Bank");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("postgres1");
        BankingApp ba = new BankingApp();
        ba.run();
    }

    public BankingApp() {
//        customerDao = new JdbcCustomerDao(dataSource);
//        accountDao = new JdbcAccountDao(dataSource);
//        transactionDao = new JdbcTransactionDao(dataSource);
    }
}



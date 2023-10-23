package org.example;

import org.example.dao.*;
import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class Session {
    private Customer currentCustomer = null;
    protected boolean userIsNew;
    private final UserInterface ui;
    private String name;
    private String password;
    private final CustomerDao cd = new JdbcCustomerDao();
    private final AccountDao ad = new JdbcAccountDao();
    private final TransactionDao td = new JdbcTransactionDao();


    public Session(UserInterface ui) {
        this.ui = ui;
    }

    public Customer welcome() {
        printOpening();
        Customer currentCustomer = null;
        while (true) {
            ui.put("Please log in by entering your 'Firstname Lastname': ");
            name = ui.getAlpha();
            ui.put("Please enter your password: ");
            password = ui.get();
            currentCustomer = lookUpCustomer();
            if (currentCustomer == null) {
                ui.put("Access Denied. Username and Password do not match our records. " +
                        "Please (T)ry again or (C)reate a new user profile: ");
                String choice = ui.getAlpha();
                if (choice.equalsIgnoreCase("C")) {
                    currentCustomer = createCustomer();
                    cd.createCustomer(currentCustomer);
                    userIsNew = true;
                }
            }
            break;
        }
        currentCustomer.setTier();
        System.out.printf("Welcome back, %s. Your current Tier is %s.\n",
                currentCustomer.getFirstName(), currentCustomer.getTier());

        return currentCustomer;
    }

    public String promptForAccountType() {
        String accountType = "";
        System.out.printf("Would you like to open a" +
                " (C)hecking or (S)avings account today? ", currentCustomer.getFirstName());
        accountType = ui.getAlpha().toLowerCase();
        return accountType;

    }

    public void printOpening() {
        ui.put("Welcome to Tech Elevator Bank!\n");
        ui.put("===$$=================Bank Policies===================$$===\n");
        ui.put("1. Savings accounts must maintain a minimum balance of $100. Withdrawals below this balance will incur a $10 fee, as well as a monthly maintenance fee of $15.");
        ui.put("2. Savings accounts may only have two withdrawals per banking session.");
        ui.put("3. Banking Loyalty tier structure: ");
        ui.put("    -Bronze: Total Balance of all accounts below $5000. Savings interest rate: 2%");
        ui.put("    -Silver: Total Balance of all accounts below $10000. Savings interest rate: 3%");
        ui.put("    -Gold: Total Balance of all accounts below $25000. Savings interest rate: 4%");
        ui.put("    -Platinum: Total Balance of all accounts at or above $25000. Savings interest rate: 5%");
        ui.put("Tiers are calculated at the conclusion of each banking session.");
        ui.put("-------------------------------------------------------------");
    }

    public Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPassword(password);
        return cd.createCustomer(customer);
    }

    public Customer lookUpCustomer() {
        try {
            currentCustomer = cd.getCustomerByNameAndPassword(name, password);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return currentCustomer;
    }

    public org.example.model.Account createAccount(String accountType) {
        while (true) {
            switch (accountType) {
                case "c":
                    accountType = "Checking";
                    org.example.model.Account checking = new org.example.model.Account(currentCustomer.getCustomerId(), accountType);
                    return ad.createAccount(checking);
                case "s":
                    accountType = "Savings";
                    org.example.model.Account savings = new Account(currentCustomer.getCustomerId(), accountType);
                    return ad.createAccount(savings);
            }
        }
    }

    public Account returnAccount(boolean customerIsNew, String accountType) {
        Account currentAccount = null;
        if (customerIsNew) {
            currentAccount = createAccount(accountType);
        } else {
            currentAccount = selectAccount();
        }
        return currentAccount;
    }

    public Account selectAccount() {
        Account currentAccount = null;
        ui.put("Please select an available account for banking: ");
        List<Account> accounts = cd.getAccountsByCustomerId(currentCustomer.getCustomerId());
        for (int i = 0; i < accounts.size(); i++) {
            ui.put(i + 1 + " " + accounts.get(i).getAccountType() + " # " + accounts.get(i).getAccountNumber());
        }
        while (true){
        int intSelection = ui.getInt();
            for (Account account : accounts) {
                if (account.getAccountNumber() == intSelection - 1) {
                    currentAccount = account;
                    return currentAccount;
                } else {
                    ui.put("Please select an available account");
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
package org.example;

import org.example.dao.*;
import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;

public class Session {
    private Customer currentCustomer = null;
    private Account currentAccount = null;
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
        ui.put("1. Savings accounts must maintain a minimum balance of $100. " +
                "Withdrawals below this balance will incur a $10 fee, as well as a monthly maintenance fee of $15.");
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

    public Account createOrSelectAccount(boolean customerIsNew, String accountType) {
        if (customerIsNew) {
            currentAccount = createAccount(accountType);
        } else {
            currentAccount = selectAccount();
        }
        return currentAccount;
    }

    public Account createAccount(String accountType) {
        while (true) {
            switch (accountType) {
                case "c":
                    accountType = "Checking";
                    Account checking = new Account(currentCustomer.getCustomerId(), accountType);
                    currentAccount = ad.createAccount(checking);
                    return currentAccount;
                case "s":
                    accountType = "Savings";
                    Account savings = new Account(currentCustomer.getCustomerId(), accountType);
                    currentAccount = ad.createAccount(savings);
                    return currentAccount;
            }
        }
    }

    public Account selectAccount() {
        Account currentAccount = null;
        ui.put("Please select an available account for banking: ");
        List<Account> accounts = cd.getAccountsByCustomerId(currentCustomer.getCustomerId());
        for (int i = 0; i < accounts.size(); i++) {
            ui.put(i + 1 + " " + accounts.get(i).getAccountType() + " # " + accounts.get(i).getAccountNumber());
        }
        while (true) {
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
    public void transact() {
        while (true) {
            if (currentAccount.getBalance().compareTo(new BigDecimal(0)) == 0) {
                System.out.printf("Your %s account has a balance of $0.\n", currentAccount.getAccountType());
                ui.put("Please enter deposit amount: ");
                BigDecimal bigCredit = ui.getBigDec();
                currentAccount.deposit(bigCredit);
                String typeAmount = "Initial Deposit $" + bigCredit;
                log.logEntry(currentUser, currentAccount, typeAmount);
            }
            if (currentCustomer.userAccounts.size() == 1) {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (T)ransfer between accounts," +
                        " (L)ist accounts, (S)witch accounts or (O)pen another account? ");
            }
            String choice = ui.getAlpha().toLowerCase();

            transactionAction(choice);
            keepBanking();
        }
        currentCustomer.setTier();
        ui.put("Banking session complete.");
    }
    public void keepBanking() {
        while (true) {
            ui.put("Current account is " + currentCustomer.currentAccount.toString());
            ui.put("Would you like to keep banking? (y/n)");
            String response = ui.getAlpha();
            if (response.equalsIgnoreCase("n")) {
                isBanking = false;
                break;
            } else if (response.equalsIgnoreCase("y")) {
                break;
            } else {
                ui.put("Please enter y or n.");
            }
        }
    }
    public void transactionAction(String choice) {
        String typeAmount = "";
        switch (choice) {
            case "w":
                ui.put("Please enter withdrawal amount: ");
                BigDecimal bigDebit = ui.getBigDec();
                BigDecimal tempBalance = currentAccount.getBalance();
                currentAccount.withdraw(bigDebit);
                BigDecimal withdrawal = tempBalance.subtract(currentAccount.getBalance());
                typeAmount = "Withdraw $" + withdrawal;
                break;
            case "d":
                ui.put("Please enter deposit amount: ");
                BigDecimal bigCredit = ui.getBigDec();
                currentAccount.deposit(bigCredit);
                ui.put("Current balance is $" + currentAccount.getBalance());
                typeAmount = "Deposit $" + bigCredit;
                break;
            case "g":
                ui.put("Your current balance is $" + currentAccount.getBalance());
                return;
            case "t":
                ui.put("Please provide transfer amount: ");
                BigDecimal transferBig = ui.getBigDec();
                if (transferBig.compareTo(currentUser.currentAccount.getBalance()) > 0) {
                    ui.put("Amount must be less than current balance.");
                    return;
                }
                org.example.Account accountTo = currentAccount.pickTransferAccount(currentUser);
                if (accountTo == null) {
                    return;
                }
                boolean enoughFunds = currentAccount.transfer(currentUser, accountTo, transferBig);
                if (!enoughFunds) return;
                String typeAmountFrom = "Transfer -$" + transferBig;
                typeAmount = "Transfer $" + transferBig;
                log.logEntry(currentUser, currentAccount, typeAmountFrom);
                log.logEntry(currentUser, accountTo, typeAmount);
                return;
            case "o":
                currentAccount = currentUser.createAccount();
                return;
            case "l":
                ui.put("Total Balance: $ " + currentUser.getTotalBalance());
                return;
            case "s":
                currentUser.selectAccount();
                return;
            default:
                ui.put("Please select an option from the list.");
                transact();
        }
        log.logEntry(currentUser, currentUser.currentAccount, typeAmount);
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
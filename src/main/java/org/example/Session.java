package org.example;

import org.example.dao.*;
import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public String promptForAccountType() {
        String accountType = "";
        System.out.printf("Would you like to open a" +
                " (C)hecking or (S)avings account today? ", currentCustomer.getFirstName());
        accountType = ui.getAlpha().toLowerCase();
        return accountType;
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
        if (ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber()).compareTo(new BigDecimal(0)) == 0) {
            System.out.printf("Your %s account has a balance of $%s.\n", currentAccount.getAccountType(),
                    ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber()));
            transactionAction("d");
        } else {
            loopSession();
        }
    }

    public void loopSession() {
        boolean continueSession = true;
        while (continueSession) {
            ui.put("Current account is " + currentAccount.toString());
            if (this.cd.getAccountsByCustomerId(currentCustomer.getCustomerId()).size() == 1) {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (T)ransfer between accounts," +
                        " (L)ist total balance, (S)witch accounts or (O)pen another account? ");
            }
            String choice = ui.getAlpha().toLowerCase();
            transactionAction(choice);
            continueSession = promptToContinue();
        }
        currentCustomer.setTier();
        ui.put("Banking session complete.");
    }

    public boolean promptToContinue() {
        boolean keepBanking = true;
        while (true) {
            ui.put("Would you like to keep banking? (y/n)");
            String response = ui.getAlpha();
            if (response.equalsIgnoreCase("n")) {
                keepBanking = false;
                break;
            } else if (response.equalsIgnoreCase("y")) {
                break;
            } else {
                ui.put("Please enter y or n.");
            }
        }
        return keepBanking;
    }

    public Transaction processTransaction(BigDecimal amount) {
        Transaction newTransaction = new org.example.model.Transaction();
        newTransaction.setCustomerId(currentCustomer.getCustomerId());
        newTransaction.setPreviousBalance(this.ad.getAccountBalanceByAccountNumber(currentAccount.getCustomerId()));
        newTransaction.setTime(LocalDate.now());
        newTransaction.setAmount(amount);
        newTransaction.setAccountNumber(currentAccount.getAccountNumber());
        return td.createTransaction(newTransaction);
    }

    public void transactionAction(String choice) {
        switch (choice) {
            case "w":
                withdraw();
                break;
            case "d":
                deposit();
                break;
            case "g":
                break;
            case "t":
                Account transferTo = pickTransferAccount();
                BigDecimal amount = promptForTransferAmount();
                transfer(transferTo, amount);
                return;
            case "o":
                currentAccount = this.createAccount(promptForAccountType());
                return;
            case "l":
                ui.put("Total Balance: $ " + cd.getTotalBalanceByCustomerId(currentCustomer.getCustomerId()));
                return;
            case "s":
                this.selectAccount();
                return;
            default:
                ui.put("Please select an option from the list.");
                loopSession();
        }
        ui.put("Current balance is $" + ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber()));
    }

    private BigDecimal promptForTransferAmount() {
        BigDecimal transferBig;
        while (true) {
            ui.put("Please provide transfer amount: ");
            transferBig = ui.getBigDec();
            if (transferBig.compareTo((this.ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber()))) > 0) {
                ui.put("Amount must be less than current balance.");
            } else {
                break;
            }
        }
        return transferBig;
    }

    public Account pickTransferAccount() {
        ui.put("Current account :#" + currentAccount.getAccountNumber() +
                " Balance $" + ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber()));
        ui.put("Available accounts to transfer to: ");
        List<Account> accounts = cd.getAccountsByCustomerId(currentCustomer.getCustomerId());
        for (Account account : accounts) {
            if (account != currentAccount)
                ui.put(account.toString());
        }
        Account accountTo = null;
        ui.put("Please enter the account number to transfer to: ");
        int acctNumToTransferTo;
        while (true) {
            acctNumToTransferTo = ui.getInt();
            for (Account account : accounts) {
                if (account.getAccountNumber() == acctNumToTransferTo) {
                    accountTo = account;
                    return accountTo;
                }
            }
            ui.put("Account not found.");
        }
    }

    public boolean transfer(Account accountTo, BigDecimal transferBig) {
        BigDecimal debit = transferBig.multiply(BigDecimal.valueOf(-1));
        this.processTransaction(debit);
        Account temp = currentAccount;
        currentAccount = accountTo;
        this.processTransaction(transferBig);
        currentAccount = temp;
        ui.put("Transfer completed");
        return true;

    }

    private BigDecimal withdraw() {
        ui.put("Please enter withdrawal amount: ");
        BigDecimal bigDebit = ui.getBigDec();
        BigDecimal tempBalance = ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber());
        BigDecimal test = tempBalance.subtract(bigDebit);
        if (test.compareTo(BigDecimal.ZERO) < 0) {
            ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + tempBalance);
            processTransaction(tempBalance.multiply(BigDecimal.valueOf(-1)));
        } else {
            processTransaction(bigDebit.multiply(BigDecimal.valueOf(-1)));
        }
        BigDecimal newBalance = ad.getAccountBalanceByAccountNumber(currentAccount.getAccountNumber());
        ui.put("Current balance is: $" + newBalance);
        return newBalance;
    }

    public void deposit() {
        ui.put("Please enter deposit amount: ");
        BigDecimal bigCredit = ui.getBigDec();
        processTransaction(bigCredit);
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
package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.dao.*;
import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Session {
    private Customer currentCustomer = null;
    private Account currentAccount = null;
    protected boolean customerIsNew = false;
    private final UserInterface ui;
    private String name;
    private String password;
    private CustomerDao cd;
    private AccountDao ad;
    private TransactionDao td;
    private int withdrawalCounter = 0;
    private DecimalFormat df = new DecimalFormat("#,###.00");


    public Session(UserInterface ui) {
        this.ui = ui;
    }

    public void setup() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/Bank");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        cd = new JdbcCustomerDao(dataSource);
        ad = new JdbcAccountDao(dataSource);
        td = new JdbcTransactionDao(dataSource);
//        ScheduleTask scheduler = new ScheduleTask();
//        scheduler.accrueInterest();
//        scheduler.chargeMaintenanceFee();
        printOpening();
        welcome();
    }

    public void printOpening() {
        ui.put("                        Welcome!\n");
        ui.put("===$$=================Bank Policies===================$$===\n");
        ui.put("1. Savings accounts must maintain a minimum balance of $500. " +
                "Withdrawals below this balance will incur a $10 fee, as well as a monthly maintenance fee of $15.");
        ui.put("2. Savings accounts may only have two withdrawals per banking session.");
        ui.put("3. Banking Loyalty tier structure: ");
        ui.put("    -Bronze: Total Balance of all accounts below $5,000. Savings interest rate: 2%");
        ui.put("    -Silver: Total Balance of all accounts below $10,000. Savings interest rate: 3%");
        ui.put("    -Gold: Total Balance of all accounts below $25,000. Savings interest rate: 4%");
        ui.put("    -Platinum: Total Balance of all accounts at or above $25,000. Savings interest rate: 5%");
        ui.put("4. Interest is accrued monthly");
        ui.put("-------------------------------------------------------------");
    }

    public Customer welcome() {
        while (true) {
            ui.put("Please log in by entering your 'Firstname Lastname': ");
            name = ui.getAlpha();
            ui.put("Please enter your password (must be at least 12 characters): ");
            password = ui.getPassword();
            currentCustomer = lookUpCustomer();
            if (currentCustomer == null) {
                ui.put("Access Denied. Please (T)ry again or (C)reate a new customer profile: ");
                String choice = ui.getAlpha();
                if (choice.equalsIgnoreCase("C")) {
                    currentCustomer = createCustomer();
                    ui.put("Profile created. Welcome, " + currentCustomer.getFirstName() + "!");
                    customerIsNew = true;
                    break;
                }
            } else {
                break;
            }
        }
        return currentCustomer;
    }

    public void promptForAccountType() {
        ui.put("Would you like to open a" +
                " (C)hecking or (S)avings account today? ");
        String accountType = ui.getAlpha().toLowerCase();
        while ((!(accountType.equalsIgnoreCase("c") || accountType.equalsIgnoreCase("s")))){
            ui.put("Please enter C or S");
            accountType = ui.getAlpha().toLowerCase();
        }
        createAccount(accountType);
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

    public Account greetAndSelect() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.printf("Welcome, %s. Your current Tier is %s.\nLast Login: %s\n",
                currentCustomer.getFirstName(), currentCustomer.getTier(), currentCustomer.getLastLogin().format(dtf));
        currentCustomer.setLastLogin(LocalDateTime.now());
        cd.updateCustomer(currentCustomer);
        currentAccount = displayAllAccounts();

        return currentAccount;
    }

    public Account createAccount(String accountType) {
        if (accountType.equalsIgnoreCase("c")) {
            accountType = "Checking";
            Account checking = new Account();
            checking.setAccountType(accountType);
            checking.setCustomerId(currentCustomer.getCustomerId());
            currentAccount = ad.createAccount(checking);
        } else if (accountType.equalsIgnoreCase("s")) {
            accountType = "Savings";
            Account savings = new Account();
            savings.setAccountType(accountType);
            savings.setCustomerId(currentCustomer.getCustomerId());
            currentAccount = ad.createAccount(savings);
        }
        return currentAccount;
    }

    public Account displayAllAccounts() {
        ui.put("Total Balance: $" + df.format((currentCustomer.getTotalBalance())));
        ui.put("\n");
        ui.put("Please enter the account number to access: ");
        List<Account> accounts = cd.getAccountsByCustomerId(currentCustomer.getCustomerId());
        for (Account account : accounts) {
            ui.put(account.toString());
        }
        while (true) {
            int intSelection = ui.getInt();
            for (Account account : accounts) {
                if (account.getAccountNumber() == intSelection) {
                    currentAccount = account;
                    return currentAccount;
                }
            }
            ui.put("Please select an available account");
        }
    }

    public void transact() {
        if (currentAccount.getAccountBalance().compareTo(new BigDecimal(0)) == 0) {
            System.out.printf("Your %s account has a balance of $%s.\n", currentAccount.getAccountType(),
                    df.format(currentAccount.getAccountBalance()));
            transactionAction("d");
        }
        loopSession();
    }

    public void loopSession() {
        boolean continueSession = true;
        while (continueSession) {
            ui.put("Current account is " + currentAccount.toString());
            ui.put("Previous transactions: ");
            for (Transaction transaction : td.getTransactionsByAccountNumber(currentAccount.getAccountNumber())) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ui.put(transaction.getTime().toLocalDateTime().format(dtf) + " Account #"+
                        transaction.getAccountNumber() + " $"+ transaction.getAmount());
            }
            if (this.cd.getAccountsByCustomerId(currentCustomer.getCustomerId()).size() == 1) {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("Would you like to (W)ithdraw funds, (D)eposit funds, (T)ransfer between accounts," +
                        " (L)ist all accounts, (S)witch accounts or (O)pen another account? ");
            }
            String choice = ui.getAlpha().toLowerCase();
            transactionAction(choice);
            ui.put("Current account is " + currentAccount.toString());
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
        Transaction newTransaction = new Transaction();
        newTransaction.setPreviousBalance(currentAccount.getAccountBalance());
        newTransaction.setTime(Timestamp.from(Instant.now()));
        newTransaction.setAmount(amount);
        newTransaction.setAccountNumber(currentAccount.getAccountNumber());
        Transaction updatedTransaction = td.createTransaction(newTransaction);
        currentAccount = ad.getAccountById(currentAccount.getAccountNumber());
        return updatedTransaction;
    }

    public void transactionAction(String choice) {
        switch (choice) {
            case "w":
                ui.put("Please enter withdrawal amount");
                BigDecimal withdrawal = ui.getBigDec();
                if (currentAccount.getAccountType().equals("Checking")) checkingWithdraw(withdrawal);
                else savingsWithdraw(withdrawal);
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
                this.promptForAccountType();
                return;
            case "l":
            case "s":
                displayAllAccounts();
                return;
            default:
                ui.put("Please select an option from the list.");
                loopSession();
        }
        ui.put("Current balance is $" + df.format(currentAccount.getAccountBalance()));
    }


    private BigDecimal promptForTransferAmount() {
        BigDecimal transferBig;
        while (true) {
            ui.put("Please provide transfer amount: ");
            transferBig = ui.getBigDec();
            if (transferBig.compareTo(currentAccount.getAccountBalance()) > 0) {
                ui.put("Amount must be less than current balance.");
            } else {
                break;
            }
        }
        return transferBig;
    }

    public Account pickTransferAccount() {
        ui.put("Current account :#" + currentAccount.getAccountNumber() +
                " Balance $" + df.format(currentAccount.getAccountBalance()));
        ui.put("Available accounts to transfer to: ");
        List<Account> accounts = cd.getAccountsByCustomerId(currentCustomer.getCustomerId());
        for (Account account : accounts) {
            if (account.getAccountNumber() != currentAccount.getAccountNumber())
                ui.put(account.toString());
        }
        Account accountTo;
        int acctNumToTransferTo;
        while (true) {
            ui.put("Please enter the account number to transfer to: ");
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

    public BigDecimal checkingWithdraw(BigDecimal bigDebit) {
        BigDecimal tempBalance = currentAccount.getAccountBalance();
        BigDecimal test = tempBalance.subtract(bigDebit);
        if (test.compareTo(BigDecimal.ZERO) < 0) {
            ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + df.format(tempBalance));
            processTransaction(tempBalance.multiply(BigDecimal.valueOf(-1)));
        } else {
            processTransaction(bigDebit.multiply(BigDecimal.valueOf(-1)));
        }
        return currentAccount.getAccountBalance();
    }

    public BigDecimal savingsWithdraw(BigDecimal bigDebit) {
        final int SAVINGS_WITHDRAWAL_MAX = 2;
        final BigDecimal MINIMUM_BALANCE = new BigDecimal(500);
        final BigDecimal OVERDRAFT_FEE = new BigDecimal(10);

        if (withdrawalCounter == SAVINGS_WITHDRAWAL_MAX) {
            ui.put("You have reached the maximum number of allowed withdrawals per session from a savings account.");
            return currentAccount.getAccountBalance();
        }

        if (currentAccount.getAccountBalance().subtract(bigDebit).compareTo(MINIMUM_BALANCE) < 0) {
            if (currentAccount.getAccountBalance().subtract(bigDebit).compareTo(BigDecimal.ZERO) < 0) {
                ui.put("Withdrawals equal or greater than current balance are not allowed. Transaction failed");
                return currentAccount.getAccountBalance();
            } else {
                processTransaction((bigDebit.add(OVERDRAFT_FEE)).multiply(BigDecimal.valueOf(-1)));
                ui.put("Your balance fell below the minimum balance of $100 and is assessed a fee of $10.");
                ui.put("Your current balance is $" + df.format(currentAccount.getAccountBalance()));
            }
        } else {
            this.checkingWithdraw(bigDebit);
        }
        withdrawalCounter++;
        System.out.printf("You have %d remaining withdrawals today. ", SAVINGS_WITHDRAWAL_MAX - withdrawalCounter);

        return currentAccount.getAccountBalance();
    }

    public void deposit() {
        ui.put("Please enter deposit amount: ");
        BigDecimal bigCredit = ui.getBigDec();
        processTransaction(bigCredit);
    }


}
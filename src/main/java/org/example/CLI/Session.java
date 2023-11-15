package org.example.CLI;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.ScheduleTask;
import org.example.report.Logger;
import org.example.report.Report;
import org.example.dao.*;
import org.example.model.Account;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.workflow.AccountHandler;
import org.example.workflow.Authentication;
import org.example.workflow.TransactionHandler;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Session {
    private Customer currentCustomer;
    private Account currentAccount;
    private boolean customerIsNew = false;
    private final UserInterface ui;
    private CustomerDao cd;
    private AccountDao ad;
    private TransactionDao td;
    private LogDao ld;
    private final DecimalFormat df = new DecimalFormat("#,###.00");


    public Session(UserInterface ui) {
        this.ui = ui;
    }

    public void start() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/Bank");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        cd = new JdbcCustomerDao(dataSource);
        ad = new JdbcAccountDao(dataSource);
        td = new JdbcTransactionDao(dataSource);
        ld = new JdbcLogDao(dataSource);
        ScheduleTask scheduler = new ScheduleTask(ad,cd,td,ld);
        scheduler.runMonthlyTasks();
        printOpening();
        currentCustomer = createOrAccessProfile();
        if (customerIsNew) {
            promptForAccountType();
        } else {
            greetAndSelect();
        }
        transact();
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

    public Customer createOrAccessProfile() {
        while (true) {
            ui.put("Please choose an option: \n1. Log in\n2. Register");
            customerIsNew = ui.getOneOrTwo();
            ui.put("Please enter your 'Firstname Lastname': ");
            String name = ui.getAlpha();
            ui.put("Please enter your password (must be at least 12 characters): ");
            String password = ui.getPassword();
            Authentication authentication = new Authentication(name, password, cd, customerIsNew);
            currentCustomer = authentication.authenticate();
            if (currentCustomer == null) {
                ui.put("Access Denied.");
            } else break;
        }
        return currentCustomer;
    }

    public void promptForAccountType() {
        ui.put("Would you like to open a" +
                " (C)hecking or (S)avings account today? ");
        String accountType = ui.getAlpha().toLowerCase();
        while ((!(accountType.equalsIgnoreCase("c") || accountType.equalsIgnoreCase("s")))) {
            ui.put("Please enter C or S");
            accountType = ui.getAlpha().toLowerCase();
        }
        createAccount(accountType);
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
            AccountHandler handler = new AccountHandler(currentCustomer.getCustomerId(), accountType, ad);
            currentAccount = handler.createAccount();
        } else if (accountType.equalsIgnoreCase("s")) {
            accountType = "Savings";
            BigDecimal initialDeposit = ui.getInitialSavingsDeposit();
            AccountHandler handler = new AccountHandler(currentCustomer.getCustomerId(),accountType, ad);
            currentAccount = handler.createAccount();
            TransactionHandler deposit = new TransactionHandler(currentAccount.getAccountNumber(), initialDeposit, ad, td);
            deposit.processTransaction();
        }
        return currentAccount;
    }

    public Account displayAllAccounts() {
        ui.put("Total Balance: $" + df.format((cd.getCustomerById(currentCustomer.getCustomerId()).getTotalBalance())));
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
            ui.put("Current account is " + ad.getAccountById(currentAccount.getAccountNumber()).toString());
            ui.put("\nPrevious transactions: \n");
            for (Transaction transaction : td.getTransactionsByAccountNumber(currentAccount.getAccountNumber())) {
                ui.put(transaction.toString());
            }
            if (this.cd.getAccountsByCustomerId(currentCustomer.getCustomerId()).size() == 1) {
                ui.put("\nWould you like to (W)ithdraw funds, (D)eposit funds, (G)et balance, or (O)pen another account? ");
            } else {
                ui.put("\nWould you like to (W)ithdraw funds, (D)eposit funds, (T)ransfer between accounts," +
                        " (L)ist all accounts, (S)witch accounts or (O)pen another account? ");
            }
            String choice = ui.getAlpha().toLowerCase();
            transactionAction(choice);
            ui.put("Current account is " + ad.getAccountById(currentAccount.getAccountNumber()).toString());
            continueSession = promptToContinue();
        }
        currentCustomer.setTier();
        ui.put("Banking session complete.");
    }

    public boolean promptToContinue() {
        boolean keepBanking = true;
        ui.put("Would you like to keep banking? (y/n)");
        String response = ui.getYesNo();
        if (response.equalsIgnoreCase("n")) {
            keepBanking = false;
        }
        return keepBanking;
    }


    public void transactionAction(String choice) {
        switch (choice) {
            case "r":
                promptForReport();
                break;
            case "w":
                final int SAVINGS_WITHDRAWAL_MAX = 5;
                ui.put("Please enter withdrawal amount");
                BigDecimal withdrawal = ui.getBigDec();
                BigDecimal previousBalance = currentAccount.getAccountBalance();
                TransactionHandler handler = new TransactionHandler(currentAccount.getAccountNumber(), withdrawal, ad, td);
                BigDecimal result = handler.withdrawCheckingOrSavings();
                if (result.compareTo(BigDecimal.ZERO) == 0) {
                    ui.put("Insufficient funds. Your account was debited the maximum allowed amount of $" + df.format(previousBalance));
                } else if (result.compareTo(new BigDecimal("-2.0")) == 0) {
                    ui.put("Your balance fell below the minimum balance of $500 and is assessed a fee of $10.");
                } else if (result.compareTo(new BigDecimal("-1.0")) == 0) {
                    ui.put("Transaction failed. Withdrawals equal or greater than current balance are not allowed.");
                } else if (result.compareTo(new BigDecimal("-3.0")) == 0) {
                    ui.put("Transaction failed. You have exceeded the maximum number of allowed withdrawals per session from a savings account.");
                }
                if ((ad.getAccountById(currentAccount.getAccountNumber())).getAccountType().equals("Savings")) {
                    System.out.printf("Savings account has %d remaining withdrawals this month. ", SAVINGS_WITHDRAWAL_MAX - (ad.getAccountById(currentAccount.getAccountNumber())).getWithdrawalCount());
                }
                break;
            case "d":
                ui.put("Please enter deposit amount: ");
                BigDecimal bigCredit = ui.getBigDec();
                handler = new TransactionHandler(currentAccount.getAccountNumber(), bigCredit, ad, td);
                handler.processTransaction();
                break;
            case "g":
                break;
            case "t":
                Account transferTo = pickTransferAccount();
                BigDecimal amount = promptForTransferAmount();
                handler = new TransactionHandler(currentAccount.getAccountNumber(), amount, ad, td);
                handler.transfer(transferTo);
                ui.put("Transfer completed");
                return;
            case "o":
                this.promptForAccountType();
                return;
            case "l":
                displayAllAccounts();
            case "s":
                displayAllAccounts();
                return;
            default:
                ui.put("Please select an option from the list.");
                loopSession();
        }
        ui.put("Current balance is $" + df.format(ad.getAccountById(currentAccount.getAccountNumber()).getAccountBalance()));
    }

    private void promptForReport() {
        Report report = new Report(ui, td);
        List<Transaction> transactions = report.getReportParameters();
        for (Transaction transaction : transactions) {
            ui.put(transaction.toString());
        }
        ui.put("Would you like to save these records? (y/n): ");
        String response = ui.getYesNo();
        if (response.equalsIgnoreCase("Y")) {
            Logger logger = new Logger();
            ui.put("Please enter new filename (no whitespace): ");
            String filePath = ui.getString();
            ui.put("Please enter file format: ");
            ui.put("1. .txt \n2. .csv");
            int format = ui.getInt();
            String extension = "";
            if (format >= 2) {
                extension += ".csv";
            } else {
                extension += ".txt";
            }
            logger.writeFile(transactions, filePath, extension);
        }
        ui.put("Records saved");

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
        ui.put("Current account: #" + ad.getAccountById(currentAccount.getAccountNumber()));
        ui.put("Available accounts to transfer to: ");
        List<Account> accounts = cd.getAccountsByCustomerId((currentAccount.getCustomerId()));
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
}
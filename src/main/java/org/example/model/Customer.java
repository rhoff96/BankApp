package org.example.model;

import org.example.CheckingAccount;
import org.example.dao.AccountDao;
import org.example.dao.CustomerDao;
import org.example.dao.JdbcAccountDao;
import org.example.dao.JdbcCustomerDao;
import java.math.BigDecimal;

public class Customer {
    private int customerId;
    private String name;
    private String password;
    private Tier tier;
    private final CustomerDao customerDao = new JdbcCustomerDao();
    private final AccountDao accountDao = new JdbcAccountDao();

    public Customer() {
    }

    public Customer(int customerId, String name, String password) {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        String[] firstAndLast = this.name.split(" ");
        return firstAndLast[0];
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

    public enum Tier {
        Bronze,
        Silver,
        Gold,
        Platinum
    }

    public void setTier() {
        if (customerDao.getTotalBalanceByCustomerId(this.getCustomerId()).compareTo(new BigDecimal("5000")) < 0) {
            this.tier = Customer.Tier.Bronze;
        } else if (customerDao.getTotalBalanceByCustomerId(this.getCustomerId()).compareTo(new BigDecimal("10000")) < 0) {
            this.tier = Customer.Tier.Silver;
        } else if (customerDao.getTotalBalanceByCustomerId(this.getCustomerId()).compareTo(new BigDecimal("25000")) < 0) {
            this.tier = Customer.Tier.Gold;
        } else {
            this.tier = Customer.Tier.Platinum;
        }
//        setInterestRate(currentUser);
    }

    public Customer.Tier getTier() {
        return tier;
    }
    @Override
    public boolean equals(Object obj) {
        Customer other = (Customer) obj;
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        if (!this.getName().equals(other.getName())) {
            return false;
        }
        if (!this.getPassword().equals(other.getPassword())) {
            return false;
        }
        return true;
    }

    //    public Account createCheckingAccount() {
//        while (true) {
//            //           System.out.printf("Welcome to our bank, %s. Would you like to open a (C)hecking or (S)avings account today? ", this.getFirstName());
//            String accountType = ui.getAlpha().toLowerCase();
//            switch (accountType) {
//                case "c":
//                    CheckingAccount checking = new CheckingAccount("Checking", (int) (Math.random() * 100000), ui);
//                    this.userAccounts.add(checking);
//                    currentAccount = checking;
//                    return currentAccount;
//                case "s":
//                    SavingsAccount savings = new SavingsAccount("Savings", (int) (Math.random() * 100000), ui);
//                    this.userAccounts.add(savings);
//                    currentAccount = savings;
//                    return currentAccount;
//                default:
//                    System.out.println("Please choose (C) or (S)");
//            }
//        }
//    }
//    public Account createAccount(String accountType) {
//        while (true) {
//            switch (accountType) {
//                case "c":
//                    accountType = "Checking";
//                    Account checking = new Account(this.getCustomerId(),accountType);
//                    return accountDao.createAccount(checking);
//                case "s":
//                    accountType = "Savings";
//                    Account savings = new Account(this.getCustomerId(), accountType);
//                    return accountDao.createAccount(savings);
//            }
//        }
//    }

//    public Account returnAccount(boolean customerIsNew, String accountType) {
//        Account currentAccount = null;
//        if (customerIsNew) {
//            currentAccount = createAccount(accountType);
//        } else {
//            currentAccount = selectAccount();
//        }
//        return currentAccount;
//    }
//
//        public Account selectAccount(String accountType) {
//        ui.put("Accounts available for banking: ");
//        for (org.example.Account account : this.userAccounts) {
//            ui.put(account.getAccountType() + " #" + account.getAccountNumber());
//        }
//        ui.put("Please enter an account number to access: ");
//        int intSelection = ui.getInt();
//        for (org.example.Account account : this.userAccounts) {
//            if (account.getAccountNumber() == intSelection) {
//                currentAccount = account;
//            }
//        }
//        return currentAccount;
//    }

}

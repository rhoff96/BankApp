package org.example;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User {

//    public Account currentAccount;
//    private String name;
//    private String password;
//    private final UserInterface ui;
//    //private Tier tier;
//    public List<Account> userAccounts = new ArrayList<>();
//
//
//    public User(String name, String password, UserInterface ui) {
//        this.name = name;
//        this.password = password;
//        this.ui = ui;
//        //this.tier = null;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getFirstName() {
//        String[] firstAndLast = this.name.split(" ");
//        return firstAndLast[0];
//    }
//
//    public String getPassword() {
//        return password;
//    }

//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

//    public Account returnAccount(boolean userIsNew) {
//        if (userIsNew) {
//            createAccount();
//        } else {
//            selectAccount();
//        }
//        return currentAccount;
//    }

//    public Account createAccount() {
//        while (true) {
//            System.out.printf("Welcome to our bank, %s. Would you like to open a (C)hecking or (S)avings account today? ", this.getFirstName());
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
//
//    public Account selectAccount() {
//        ui.put("Accounts available for banking: ");
//        for (Account account : this.userAccounts) {
//            ui.put(account.getAccountType() + " #" + account.getAccountNumber());
//        }
//        ui.put("Please enter an account number to access: ");
//        int intSelection = ui.getInt();
//        for (Account account : this.userAccounts) {
//            if (account.getAccountNumber() == intSelection) {
//                currentAccount = account;
//            }
//        }
//        return currentAccount;
//    }

//    public BigDecimal getTotalBalance() {
//        BigDecimal totalBalance = BigDecimal.ZERO;
//        for (Account account : this.userAccounts) {
//            System.out.println(account.getAccountType() + " #" + account.getAccountNumber() + ": $" + account.getBalance());
//            totalBalance = totalBalance.add(account.getBalance());
//        }
//        return totalBalance;
//    }
//
//    public enum Tier {
//        Bronze,
//        Silver,
//        Gold,
//        Platinum
//    }

//    public void setTier() {
//        if (this.getTotalBalance().compareTo(new BigDecimal("5000")) < 0) {
//            this.tier = Tier.Bronze;
//        } else if (this.getTotalBalance().compareTo(new BigDecimal("10000")) < 0) {
//            this.tier = Tier.Silver;
//        } else if (this.getTotalBalance().compareTo(new BigDecimal("25000")) < 0) {
//            this.tier = Tier.Gold;
//        } else {
//            this.tier = Tier.Platinum;
//        }
//        setInterestRate(currentUser);
//    }

//    public Tier getTier() {
//        return tier;
//    }

//    public BigDecimal setInterestRate(User currentUser) {
//        BigDecimal ir = null;
//        if (currentUser.tier == Tier.Bronze) {
//            ir = new BigDecimal(".02");
//        }
//        if (currentUser.tier == Tier.Silver) {
//            ir = new BigDecimal(".03");
//        }
//        if (currentUser.tier == Tier.Gold) {
//            ir = new BigDecimal(".04");
//        }
//        if (currentUser.tier == Tier.Platinum) {
//            ir = new BigDecimal(".05");
//        }
//        return ir;
//    }

//    @Override
//    public boolean equals(Object obj) {
//        User other = (User) obj;
//        if (!this.getClass().equals(other.getClass())) {
//            return false;
//        }
//        if (!this.getName().equals(other.getName())) {
//            return false;
//        }
//        if (!this.getPassword().equals(other.getPassword())) {
//            return false;
//        }
//        return true;
//    }
}


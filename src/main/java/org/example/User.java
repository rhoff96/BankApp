package org.example;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User {

    public Account currentAccount;
    private String name;
    private String password;
    private BigDecimal totalBalance;
    private final UserInterface ui;

    public List<Account> userAccounts = new ArrayList<>();

    public User(String name, String password, UserInterface ui) {
        this.name = name;
        this.password = password;
        this.ui = ui;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        String[] firstAndLast = this.name.split(" ");
        return firstAndLast[0];
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account createAccount(User currentUser) {
        System.out.printf("Welcome to our bank, %s. Would you like to open a (C)hecking or (S)avings account today? ", currentUser.getFirstName());
        String accountType = ui.getAlpha();
        if (accountType.equalsIgnoreCase("c")) {
            CheckingAccount checking = new CheckingAccount("Checking", (int) (Math.random() * 100000), ui);
            currentUser.userAccounts.add(checking);
            currentAccount = checking;
        } else if (accountType.equalsIgnoreCase("s")) {
            SavingsAccount savings = new SavingsAccount("Savings", (int) (Math.random() * 100000), ui);
            currentUser.userAccounts.add(savings);
            currentAccount = savings;
        } else {
            System.out.println("Please choose (C) or (S)");
            createAccount(currentUser);
        }
        return currentAccount;
    }

    public Account selectAccount(User currentUser) {
        ui.put("Accounts available for banking: ");
        for (Account account : currentUser.userAccounts) {
            ui.put(account.accountType + " #" + account.getAccountNumber());
        }
        ui.put("Please enter an account number to access: ");
        int intSelection = ui.getInt();
        for (Account account : currentUser.userAccounts) {
            if (account.getAccountNumber() == intSelection) {
                currentAccount = account;
            }
        }
        return currentAccount;
    }

    public String getTotalBalance(User currentUser) {
        totalBalance = BigDecimal.ZERO;
        for (Account account : currentUser.userAccounts) {
            System.out.println(account.accountType + " #" + account.getAccountNumber() + ": $" + account.getBalance());
            totalBalance = totalBalance.add(account.getBalance());
        }
        return "Total Balance: $" + totalBalance;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = true;
        User other = (User) obj;

        if (!this.getClass().equals(other.getClass())) {
            isEqual = false;
        }
        if (!this.getName().equals(other.getName())) {
            isEqual = false;
        }
        if (!this.getPassword().equals(other.getPassword())) {
            isEqual = false;
        }
        return isEqual;
    }
}


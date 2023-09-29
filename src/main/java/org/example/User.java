package org.example;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {

    Scanner userInput = new Scanner(System.in);
    public Account currentAccount;
    private String name;
    private String password;
    private BigDecimal totalBalance;
    private UserInterface ui;

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
            CheckingAccount checking = new CheckingAccount("Checking", (int)(Math.random()*100000), ui);
            currentUser.userAccounts.add(checking);
            currentAccount = checking;
        } else if (accountType.equalsIgnoreCase("s")) {
            SavingsAccount savings = new SavingsAccount("Savings", (int)(Math.random()*100000), ui);
            currentUser.userAccounts.add(savings);
            currentAccount = savings;
        }
        return currentAccount;
    }

    public Account selectAccount(User currentUser) {
        System.out.printf("Welcome back, %s. Accounts available for banking: ", currentUser.getFirstName());
        for (Account account : currentUser.userAccounts) {
            ui.put(account.accountType + "--" + account.getAccountNumber());
        }
        ui.put("Please an account number to access: ");
        String typeSelection = ui.getAlpha();
        int intSelection = Integer.parseInt(typeSelection);
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
        return "Total Balance: $"+ totalBalance;
    }
}


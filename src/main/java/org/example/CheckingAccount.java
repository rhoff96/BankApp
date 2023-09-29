package org.example;

public class CheckingAccount extends Account{
    private UserInterface ui;
    public CheckingAccount(String accountType, int accountNumber, UserInterface ui) {
        super(accountType, accountNumber, ui);
    }
}

package org.example;

import java.util.*;

/*
bugs:
savings withdrawal happens twice
transfers cannot allow negative balances to result
exception handling for wrong input in picking account to transfer to


Functionality to add:
assign unique bank account numbers - timestamp related?
move savings withdrawal limit functionality into savings class. super() withdraw questions?


Notes:
revise access modifiers: move variables.


Future:
Unit Tests
save transaction times
transfers between users?
 */

public class Main {
    public static void main(String[] args) {

        Account currentAccount;
        User currentUser;

        UserInterface ui = new UserInterface();

        AuthenticationContext authenticationContext = new AuthenticationContext(ui);
        authenticationContext.welcome();
        currentUser = authenticationContext.currentUser;
        if (authenticationContext.userIsNew){
            currentAccount = authenticationContext.currentUser.createAccount(currentUser);
        } else {
            currentAccount = currentUser.selectAccount(currentUser);
        }
        Transaction transact = new Transaction(currentUser, currentAccount, ui);
        transact.transact();

    }

}



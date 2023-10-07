package org.example;


public class BankingApp {

    public void run() {
        Account currentAccount;
        User currentUser;
        UserInterface ui = new UserInterface();

        AuthenticationContext authenticationContext = new AuthenticationContext(ui);
        authenticationContext.welcome();
        currentUser = authenticationContext.currentUser;

        if (authenticationContext.userIsNew) {
            currentAccount = currentUser.createAccount(currentUser);
        } else {
            currentAccount = currentUser.selectAccount(currentUser);
        }

        Transaction transact = new Transaction(currentUser, currentAccount, ui);
        transact.transact();
    }


    public static void main(String[] args) {
        BankingApp ba = new BankingApp();
        ba.run();
    }
}



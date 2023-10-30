package org.example;



public class BankingApp {


    public void run() {
        UserInterface ui = new UserInterface();
        Session session = new Session(ui);
        session.setup();
        String accountType = null;
        if (session.customerIsNew) {
            accountType = session.promptForAccountType();
        }
        session.createOrSelectAccount(session.customerIsNew);
        session.transact();
    }

    public static void main(String[] args) {
        BankingApp ba = new BankingApp();
        ba.run();
    }

}



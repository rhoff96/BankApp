package org.example;



public class BankingApp {


    public void run() {
        UserInterface ui = new UserInterface();
        Session session = new Session(ui);
        session.setup();
        session.transact();
    }

    public static void main(String[] args) {
        BankingApp ba = new BankingApp();
        ba.run();
    }

}



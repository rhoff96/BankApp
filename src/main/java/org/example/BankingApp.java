package org.example;


import org.example.CLI.Session;
import org.example.CLI.UserInterface;

public class BankingApp {


    public void run() {
        UserInterface ui = new UserInterface();
        Session session = new Session(ui);
        session.start();
    }

    public static void main(String[] args) {
        BankingApp ba = new BankingApp();
        ba.run();
    }

}



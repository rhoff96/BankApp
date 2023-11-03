package org.example;


import org.example.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BankingApp {




    public void run() {
        UserInterface ui = new UserInterface();
        Session session = new Session(ui);
        session.setup();
        if (session.customerIsNew) {
            session.promptForAccountType();
        } else {
            session.greetAndSelect();
        }
        session.transact();
    }

    public static void main(String[] args) {
        BankingApp ba = new BankingApp();
        ba.run();
    }

}



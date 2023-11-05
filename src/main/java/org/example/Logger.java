package org.example;

import org.example.model.Transaction;

import java.io.*;
import java.util.List;

public class Logger {

    public void writeFile(List<Transaction> transactions) {
        String filePath = "report.txt";
        final File f = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(f, true);
             PrintWriter writer = new PrintWriter(fos)) {
            for (Transaction transaction : transactions) {
                writer.println(transaction.getTransactionId() + " " + transaction.toString());
            }
        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }

    }
}

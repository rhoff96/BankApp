package org.example.report;

import org.example.model.Transaction;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Logger {

    public void writeFile(List<Transaction> transactions, String filepath, String extension) {
        String fullFilePath = filepath + extension;
        final File f = new File(fullFilePath);

        try (FileOutputStream fos = new FileOutputStream(f);
             PrintWriter writer = new PrintWriter(fos)) {
            for (Transaction transaction : transactions) {
                if (extension.equals(".csv")) {
                    writer.println(transaction.getTransactionId() + "," + transaction.getTime() + ","
                            + transaction.getAccountNumber() + "," + transaction.getPreviousBalance() + "," + transaction.getAmount());
                } else {
                    writer.println(transaction.getTransactionId() + " " + transaction.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }

    }

    public List<Transaction> readFile(String filepath) {
        File sourceFile = new File(filepath);
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(sourceFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
        }
        List<Transaction> transactions = new ArrayList<>();
        while (fileReader.hasNextLine()) {
            String currentLine = fileReader.nextLine();
            if (filepath.contains(".csv")) {
                String[] split = currentLine.split("\\,");
                Transaction newTransaction = new Transaction(Timestamp.valueOf(split[1]), new BigDecimal(split[3]), Integer.parseInt(split[0]), Integer.parseInt(split[2]), new BigDecimal(split[4]));
                transactions.add(newTransaction);
            } else {

            }
        }
        return transactions;

    }
}

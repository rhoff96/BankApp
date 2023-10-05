package org.example;

import java.io.*;
import java.time.LocalDateTime;

public class Log {
    final private String path = "log.txt";
    final private File f = new File(path);
    LocalDateTime lt = LocalDateTime.now();

    public void logEntry(User currentUser, Account currentAccount) {
        try (FileOutputStream fos = new FileOutputStream(f, true);
             PrintWriter writer = new PrintWriter(fos)){
            writer.println(currentUser.getName() + ": #" + currentAccount.getAccountNumber()
                    + " $" + currentAccount.getBalance() + " " + lt);
        } catch(IOException e){
            System.out.println("Error" + e.getMessage());
        }
    }
}

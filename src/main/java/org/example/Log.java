package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    final private String path = "log.txt";
    final private File f = new File(path);

    public void logEntry(User currentUser, Account currentAccount, String typeAmount) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        String date = currentTime.format(dtf);
        try (FileOutputStream fos = new FileOutputStream(f, true);
             PrintWriter writer = new PrintWriter(fos)) {
            writer.println(date + " " + currentUser.getName() + ": Account #" + currentAccount.getAccountNumber()
                    + " " + typeAmount + " Current Balance $" + currentAccount.getBalance());
        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }
    }
}

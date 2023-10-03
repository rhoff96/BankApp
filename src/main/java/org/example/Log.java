package org.example;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Log {
    private User currentUser;
    private String path = "log.txt";
    private File f = new File(path);
    LocalDateTime lt = LocalDateTime.now();

    public void createLog() {
        try (FileOutputStream fos = new FileOutputStream(f); PrintWriter writer
                = new PrintWriter(path)) {
            writer.println("Tech Elevator Bank Log " + lt);
        } catch (FileNotFoundException e) {
            System.out.println("Error" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    public void logEntry(User currentUser, Account currentAccount, BigDecimal amount) {
        try (FileOutputStream fos = new FileOutputStream(f, true);
             PrintWriter writer = new PrintWriter(fos)){
            writer.println(currentUser.getName() + ": #" + currentAccount.getAccountNumber()
                    + " $" + currentAccount.getBalance() + " " + lt);
        } catch(IOException e){
            System.out.println("Error" + e.getMessage());
        }


    }
}

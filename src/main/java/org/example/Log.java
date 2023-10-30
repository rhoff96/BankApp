package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//public class Log {
//
//    public void logEntry(User currentUser, Account currentAccount, String typeAmount) {
//        LocalDateTime currentTime = LocalDateTime.now();
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");
//        String date = currentTime.format(dtf);
//
//        final String path = "log.txt";
//        final File f = new File(path);
//
//        try (FileOutputStream fos = new FileOutputStream(f, true);
//             PrintWriter writer = new PrintWriter(fos)) {
//            writer.println(date + " " + currentUser.getName() + ": Account #" + currentAccount.getAccountNumber()
//                    + " " + typeAmount + " Current Balance $" + currentAccount.getBalance());
//        } catch (IOException e) {
//            System.out.println("Error" + e.getMessage());
//        }
//    }
//
//    public void logEntry(String filePath, User currentUser, Account currentAccount, String typeAmount) {
//        final File f = new File(filePath);
//
//        try (FileOutputStream fos = new FileOutputStream(f, true);
//             PrintWriter writer = new PrintWriter(fos)) {
//            writer.println(currentUser.getName() +": " +currentAccount.getAccountType() +" Account #" + currentAccount.getAccountNumber()
//                    + " " + typeAmount + " Current Balance $" + currentAccount.getBalance());
//        } catch (IOException e) {
//            System.out.println("Error" + e.getMessage());
//        }
//
//    }
//}

package org.example.CLI;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInterface {

    Scanner userInput = new Scanner(System.in);

    public String getString() {
        return userInput.nextLine();

    }

    public void put(String message) {
        System.out.println(message);
    }

    public String getPassword() {
        String input = userInput.nextLine();
        while (input.length() < 12) {
            System.out.println("Password must be at least 12 characters");
            input = userInput.nextLine();
        }
        return input;
    }

    public String getAlpha() {
        String response = userInput.nextLine();
        while (!Pattern.compile("[a-zA-Z\\s]*").matcher(response).matches()) {
            System.out.println("Please input only letters A-Z.");
            response = userInput.nextLine();
        }
        return response;
    }

    public String getYesNo() {
        String response = userInput.nextLine();
        while ((!response.equalsIgnoreCase("y")) && (!response.equalsIgnoreCase("n"))) {
            System.out.println("Please enter a Y or N");
            response = userInput.nextLine();
        }
        return response;
    }

    public Timestamp getTimestamp() {
        String input = userInput.nextLine();
        return Timestamp.valueOf(input);

    }

    public int getInt() {
        String response = userInput.nextLine();
        while (Pattern.compile("[^\\d]").matcher(response).find() || !response.matches("\\d+")) {
            System.out.println("Please enter an integer value");
            response = userInput.nextLine();
        }
        return Integer.parseInt(response);

    }

    public boolean getOneOrTwo() {
        String response = userInput.nextLine();
        while (!(response.equals("1")) && (!response.equals("2"))) {
            System.out.println("Please choose one of the options.");
            response = userInput.nextLine();
        }
        boolean isNew = response.equals("2");
        return isNew;
    }

    public BigDecimal getBigDec() {
        String response = userInput.nextLine();
        while (!Pattern.compile("^[+]?\\d+([.]\\d+)?$").matcher(response).matches()) {
            System.out.println("Please enter a positive numerical value");
            response = userInput.nextLine();
        }
        BigDecimal bigResponse = new BigDecimal(response);
        return roundBigDec(bigResponse);
    }

    public BigDecimal getInitialSavingsDeposit() {
        System.out.println("Please enter initial deposit amount ($500 or greater):");
        BigDecimal initialDeposit = this.getBigDec();
        while (initialDeposit.compareTo(new BigDecimal("500.0")) < 0) {
            System.out.println("Amount must be greater than or equal to $500");
            initialDeposit = this.getBigDec();
        }
        return initialDeposit;
    }

    public BigDecimal roundBigDec(BigDecimal input) {
        return input.setScale(2, RoundingMode.DOWN);

    }
}

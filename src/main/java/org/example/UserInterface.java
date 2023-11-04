package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInterface {

    Scanner userInput = new Scanner(System.in);


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

    public int getInt() {
        String response = userInput.nextLine();
        while (response.equals("") || response.contains(".")) {
            System.out.println("Please enter an integer value");
            response = userInput.nextLine();
        }
        while (Pattern.compile("[a-zA-Z\\s]*").matcher(response).matches()) {
            System.out.println("Please enter a integer value");
            response = userInput.nextLine();
        }
        return Integer.parseInt(response);

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
    public BigDecimal roundBigDec(BigDecimal input){
        return input.setScale(2, RoundingMode.DOWN);

    }
}

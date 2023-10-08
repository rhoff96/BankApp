package org.example;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInterface {

    Scanner userInput = new Scanner(System.in);


    public void put(String message) {
        System.out.println(message);
    }

    public String get() {
        return userInput.nextLine();
    }

    public String getAlpha() {
        String response = userInput.nextLine();
        while (!Pattern.compile("[a-zA-Z\\s]*").matcher(response).matches()) {
            System.out.println("Please input only letters A-Z.");
            response = userInput.nextLine();
        }
        return response;
    }
    public int getInt(){
        String response = userInput.nextLine();
        while (response.equals("")) {
            System.out.println("Please enter a numerical value");
            response = userInput.nextLine();
        }
        while (Pattern.compile("[a-zA-Z\\s]*").matcher(response).matches()) {
            System.out.println("Please enter a numerical value");
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
        return new BigDecimal(response);
    }

}

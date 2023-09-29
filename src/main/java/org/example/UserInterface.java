package org.example;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserInterface {

    Scanner userInput = new Scanner(System.in);


    public void put(String message) {
        System.out.println(message);
    }

    public String getAlpha() {
        String response = userInput.nextLine();
        while (response.equals("")) {
            System.out.println("Please enter word(s).");
            response = userInput.nextLine();
        }
        return response;
    }
    public int getNum(){
        String response = userInput.nextLine();
        while (!(Integer.parseInt(response) <= 10 && Integer.parseInt(response) > 0)){
            System.out.println("Please provide a valid number choice between 1 and 10");
            response = userInput.nextLine();
        }
        int intResponse = Integer.parseInt(response);
        return intResponse;
    }
    public BigDecimal getBigDec() {
        BigDecimal bigDec = new BigDecimal(getAlpha());
        return bigDec;
    }

}

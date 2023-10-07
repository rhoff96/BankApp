package org.example;

import java.util.*;

public class AuthenticationContext {

    private final Map<String, String> namePasswords = new HashMap<>();
    private final List<User> userList = new ArrayList<>();
    public User currentUser;
    public boolean userIsNew;
    private final UserInterface ui;


    public AuthenticationContext(UserInterface ui){
        this.ui = ui;
    }

    public void welcome() {
        printOpening();
        //hard-coding a user to test functionality
        namePasswords.put("Russell Hoffman","admin");
        User ml = new User("Russell Hoffman","admin",ui);
        ml.currentAccount = new CheckingAccount("Checking",123,ui);
        String name = ui.getAlpha();
        ui.put("Please enter your password: ");
        String password = ui.get();
        authenticate(name, password);
    }
    public void printOpening(){
        ui.put("Welcome to Tech Elevator Bank!\n");
        ui.put("===$$=================Bank Policies===================$$===\n");
        ui.put("1. Savings accounts must maintain a minimum balance of $100. Withdrawals below this balance will incur a $10 fee, as well as a monthly maintenance fee of $15.");
        ui.put("2. Savings accounts may only have two withdrawals per banking session.");
        ui.put("3. Banking Loyalty tier structure: ");
        ui.put("    -Bronze: Total Balance of all accounts below $5000. Savings interest rate: 2%");
        ui.put("    -Silver: Total Balance of all accounts below $10000. Savings interest rate: 3%");
        ui.put("    -Gold: Total Balance of all accounts below $25000. Savings interest rate: 4%");
        ui.put("    -Platinum: Total Balance of all accounts at or above $25000. Savings interest rate: 5%");
        ui.put("Tiers are calculated at the conclusion of each banking session.");
        ui.put("-------------------------------------------------------------");
        ui.put("Please log in by entering your 'Firstname Lastname': ");
    }

    public User authenticate(String name, String password) {
        if (namePasswords.isEmpty()) {
            createUser(name, password);
        } else {
            findUser(name,password);
        }
        return currentUser;
    }

    public void createUser(String name, String password) {
        User user = new User(name, password, ui);
        currentUser = user;
        user.setPassword(password);
        user.setName(name);
        userList.add(currentUser);
        namePasswords.put(name, password);
        userIsNew = true;
        currentUser.setTier(currentUser);

    }
    public void findUser(String name, String password){
        for (String username : namePasswords.keySet()) {
            if (username.equals(name) && namePasswords.get(name).equals(password)) {
                for (User user : userList) {
                    if (user.getPassword().equals(password)) {
                        currentUser = user;
                        System.out.printf("Welcome back, %s. Your current Tier is %s.\n",
                                currentUser.getFirstName(), currentUser.getTier());
                    }
                }
            } else {
                ui.put("Access Denied. Username and Password do not match our records. " +
                        "Please (T)ry again or (C)reate a new user profile: ");
                String choice = ui.getAlpha();
                if (choice.equalsIgnoreCase("T")) {
                    welcome();
                } else if (choice.equalsIgnoreCase("C")) {
                    createUser(name, password);
                }
            }
        }
    }
}
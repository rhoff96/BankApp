package org.example;

import java.util.*;

public class AuthenticationContext {

    public Map<String, String> namePasswords = new HashMap<>();
    public List<User> userList = new ArrayList<>();
    public User currentUser;
    public String name;
    public String password;
    public boolean userIsNew;
    private UserInterface ui;


    public AuthenticationContext(UserInterface ui){
        this.ui = ui;
    }

    public void welcome() {
        ui.put("Welcome to Tech Elevator Bank!\n");
        ui.put("===$$=====Bank Policies=====$$===\n");
        ui.put("1. Savings accounts must maintain a minimum balance of $100. Withdrawls below this balance will incur a $10 fee.");
        ui.put("2. Savings accounts may only have two withdrawls per banking session.\n");
        ui.put("Please log in by entering your 'Firstname Lastname': ");
        name = ui.getAlpha();
        ui.put("Please enter your password: ");
        password = ui.getAlpha();
        authenticate(name, password);
    }

    public User authenticate(String name, String password) {
        if (namePasswords.isEmpty()) {
            createUser(name, password);
        } else {
            for (String username : namePasswords.keySet()) {
                if (username.equals(name) && namePasswords.get(name).equals(password)) {
                    for (User user : userList) {
                        if (user.getPassword().equals(password)) {
                            currentUser = user;
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
        return currentUser;
    }

    public User createUser(String name, String password) {
        User user = new User(name, password, ui);
        currentUser = user;
        user.setPassword(password);
        user.setName(name);
        userList.add(currentUser);
        namePasswords.put(name, password);
        userIsNew = true;

        return currentUser;
    }
}
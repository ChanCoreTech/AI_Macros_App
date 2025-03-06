package org.aimacrosapp;
import java.io.IOException;
import java.net.MalformedURLException;
public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Now, use the token to create a new user
            AccountLogic accountLogic = new AccountLogic();

            // First, log in the user to get the JWT token
            String email = "sdoe@example.com"; // Replace with actual user input
            String password = "doe123";  // Replace with actual user input
            String jwtToken = AccountLogic.loginUser(email, password); // Get the token

            boolean isUserCreated = accountLogic.newUser("Sarah", "Doe", "1990-01-01", "Female", 5, 9, jwtToken);

            if (isUserCreated) {
                System.out.println("User created successfully!");
            } else {
                System.out.println("Failed to create user.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//        //test
//        String jwtToken = AccountLogic.loginUser("sdoe@example.com", "doe123");
//    AccountLogic a = new AccountLogic();
//        boolean success = a.newUser("Sally", "Doe", "1990-04-25", "Female", 5, 10, jwtToken);
//
//        if (success) {
//            System.out.println("User created successfully.");
//        } else {
//            System.out.println("Failed to create user.");
//        }
//    //a.newUser("Sally", "Doe", "1990-04-25", "Female", 5, 10);
    }
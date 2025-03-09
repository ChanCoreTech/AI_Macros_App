package org.aimacrosapp;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Example: Hard-coded user data
            AccountLogic accountLogic = new AccountLogic();
            String userId = accountLogic.createUser("John", "Doe", "1990-01-01", "Male", 5, 9, "medium", null, null, "lose weight");

            if(userId != null) {
                boolean userAccountCreated = accountLogic.createUserAccount("jdoe1@example.com", "jdoe123", "Cool Dood", "803-502-6589", null, userId);
            if(userAccountCreated) {
                System.out.println("User account created successfully!");
            }
            else{
                System.out.println("Failed to create user account!");
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
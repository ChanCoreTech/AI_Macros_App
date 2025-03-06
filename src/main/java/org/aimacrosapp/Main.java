package org.aimacrosapp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Example: Hard-coded user data
            AccountLogic accountLogic = new AccountLogic();
            boolean result = accountLogic.newUser("John", "Doe", "1990-01-01", "Male", 6, 2);
            System.out.println("User creation successful: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
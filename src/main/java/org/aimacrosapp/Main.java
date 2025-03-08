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
            String userId = accountLogic.newUser("Autumn", "Doe", "1990-01-01", "Female", 5, 9);

            if(userId != null) {
                boolean userAccountCreated = accountLogic.newUserAccount("adoe1@example.com", "adoe123", "Cool Girl", "803-502-6589", userId);
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
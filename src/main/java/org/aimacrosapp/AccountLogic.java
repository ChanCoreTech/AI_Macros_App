package org.aimacrosapp;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Scanner;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AccountLogic {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");

    // Method to create a new user and insert into the database (bypassing authentication)
    public String newUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches) throws IOException {
        String endpoint = DB_URL + "/rest/v1/users"; // Ensure your custom table name is correct
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY); // Using anon key for public access
        conn.setDoOutput(true);

        // Prepare JSON input for insertion into your table
        String jsonInputString = String.format(
                "{\"first_name\":\"%s\", \"last_name\":\"%s\", \"birth_date\":\"%s\", \"gender\":\"%s\", \"height_feet\":%d, \"height_inches\":%d}",
                first_name, last_name, birth_date, gender, height_feet, height_inches
        );

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            // Get the response code from the API
            int responseCode = conn.getResponseCode();

            System.out.println("Sending JSON: " + jsonInputString);

            // Handle the response based on the status code
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Success, print the response body
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) {
                        String jsonResponse = scanner.nextLine();
                        String userId = extractUserId(jsonResponse);
                        return userId; // Return the user_id if successful
                    }
                }
            } else {
                // Error handling
                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                }
                return null; // Return null if user creation failed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Catch any exceptions and return null for failure
    }


    // Method to get the user id from the JSON response
    private String extractUserId(String jsonResponse) {
        // Parse JSON to get user_id (assuming response is formatted correctly)
        jsonResponse = jsonResponse.replace("[", "").replace("]", "");
        String userId = jsonResponse.split(":")[1].replace("\"", "").trim();
        return userId; // Return the alphanumeric user_id
    }

    // Method to create a new user account and insert into the database
    public boolean newUserAccount(String email, String password, String nickname, String phone_number, String user_id) throws IOException {
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        String endpoint = DB_URL + "/rest/v1/user_account";
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY); // Using anon key for public access
        conn.setDoOutput(true);

        // Prepare JSON input for insertion into your table
        String jsonInputString = String.format(
                "{\"email\":\"%s\", \"password\":\"%s\", \"nickname\":\"%s\", \"phone_number\":\"%s\", \"user_id\":\"%s\"}",
                email, password, nickname, phone_number, user_id
        );

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            // Get the response code from the API
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Handle the response based on the status code
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Success
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                }
                return true;
            } else {
                // Error handling
                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                    StringBuilder errorResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        errorResponse.append(scanner.nextLine());
                    }
                    System.out.println("Error Response: " + errorResponse.toString());
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Catch any exceptions and return false
        }
    }
}
package org.aimacrosapp;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AccountLogic {
    //dotenv allows me to save keys in env file
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");

    //method to establish HTTP connections (user insertion only)
    public HttpURLConnection establishDatabaseConnection(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches) throws IOException {
        //endpoint is the url for the user table
        String endpoint = DB_URL + "/rest/v1/users";
        URL url = new URL(endpoint);
        //open HTTP connection for API call
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //connecting to supabase via API request to post data to user table
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setRequestProperty("apikey", DB_KEY);
        conn.setDoOutput(true);
        return conn;
    }

    //overloaded method to establish HTTP connections (user account insertion only)
    public HttpURLConnection establishDatabaseConnection(String email, String password, String nickname, String phone_number, String email_second) throws IOException {
        //endpoint is the url for the user account table
        String endpoint = DB_URL + "/rest/v1/user_account";
        URL url = new URL(endpoint);
        //open HTTP connection for API call
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //connecting to supabase via API request to post data to user account table
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setRequestProperty("apikey", DB_KEY);
        conn.setDoOutput(true);
        return conn;
    }

    // Method to get the user id from the JSON response
    private String extractUserId(String jsonResponse) {
        // Ensure response is not empty
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null;
        }
        try {
            // If response is an array, parse it as JSONArray
            JSONArray jsonArray = new JSONArray(jsonResponse);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject.getString("user_id"); // Extract "user_id"
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if extraction fails
    }

    // Method to create a new user and insert into the database (bypassing authentication)
    public String createUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches,
                             String body_type, String experience_level, String activity_level, String primary_goal ) throws IOException {
        // Prepare JSON input for insertion into user table
        String jsonInputString = String.format(
                "{\"first_name\":\"%s\", \"last_name\":\"%s\", \"birth_date\":\"%s\", \"gender\":\"%s\", \"height_feet\":%d, \"height_inches\":%d," +
                        "\"body_type\":\"%s\", \"experience_level\":\"%s\", \"activity_level\":\"%s\", \"primary_goal\":\"%s\"}",
                first_name, last_name, birth_date, gender, height_feet, height_inches, body_type, experience_level, activity_level, primary_goal
        );
        //call to HTTP connection
       HttpURLConnection conn = establishDatabaseConnection(first_name, last_name, birth_date, gender, height_feet, height_inches);

        //json response
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
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                        System.out.println("Raw JSON response: " + jsonResponse.toString());
                        String user_id = extractUserId(jsonResponse.toString());
                        System.out.println("Creating user account with user_id: " + user_id);
                        return user_id; // Return the user_id if successful
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

    // Method to create a new user account and insert into the database
    public boolean createUserAccount(String email, String password, String nickname, String phone_number, String email_second, String user_id) throws IOException {
        //if no user id provided to user account, show error msg
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        // Prepare JSON input for insertion into user account table
        String jsonInputString = String.format(
                "{\"email\":\"%s\", \"password\":\"%s\", \"nickname\":\"%s\", \"phone_number\":\"%s\", \"user_id\":\"%s\", \"email_second\":\"%s\"}",
                email, password, nickname, phone_number, user_id, email_second
        );

        //call to HTTP connection
        HttpURLConnection conn = establishDatabaseConnection(email, password, nickname, phone_number, email_second);

        //JSON code
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            // Get the response code from the API
            int responseCode = conn.getResponseCode();
            //System.out.println("Response Code: " + responseCode);

            System.out.println("Sending JSON: " + jsonInputString);
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

    // Method to update a user account
    public String updateUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches,
                             String body_type, String experience_level, String activity_level, String primary_goal ) throws IOException {
        // Prepare JSON input for insertion into user table
        String jsonInputString = String.format(
                "{\"first_name\":\"%s\", \"last_name\":\"%s\", \"birth_date\":\"%s\", \"gender\":\"%s\", \"height_feet\":%d, \"height_inches\":%d," +
                        "\"body_type\":\"%s\", \"experience_level\":\"%s\", \"activity_level\":\"%s\", \"primary_goal\":\"%s\"}",
                first_name, last_name, birth_date, gender, height_feet, height_inches, body_type, experience_level, activity_level, primary_goal
        );
        //call to HTTP connection
        HttpURLConnection conn = establishDatabaseConnection(first_name, last_name, birth_date, gender, height_feet, height_inches);

        //json response
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
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                    System.out.println("Raw JSON response: " + jsonResponse.toString());
                    String user_id = extractUserId(jsonResponse.toString());
                    System.out.println("Creating user account with user_id: " + user_id);
                    return user_id; // Return the user_id if successful
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



}
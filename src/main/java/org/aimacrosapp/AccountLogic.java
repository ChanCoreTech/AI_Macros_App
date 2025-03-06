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
    public boolean newUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches) throws IOException {
        String endpoint = DB_URL + "/rest/v1/users"; // Ensure your custom table name is correct
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY); // Using anon key for public access
        conn.setDoOutput(true);

        // Print the input values for debugging
        System.out.println("Inserting user: " + first_name + " " + last_name);

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
            System.out.println("Response Code: " + responseCode);

            // Handle the response based on the status code
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Success, print the response body
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                }
                return true; // Indicating the user was created successfully
            } else {
                // Error handling
                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                }
                return false; // Indicating failure
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Catch any exceptions and return false
        }
    }
}
//    private static final Dotenv dotenv = Dotenv.load();
//    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
//    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");
//
//    // Method to create a user (using JWT token for authorization)
//    public boolean newUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches,
//                           String jwtToken) throws IOException {
//        String endpoint = DB_URL + "/rest/v1/users";
//        URL url = new URL(endpoint);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("apikey", DB_KEY);
//        conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
//        conn.setDoOutput(true);
//
//        System.out.println("JWT Token: " + jwtToken);
//
//
//        String jsonInputString = String.format(
//                "{\"first_name\":\"%s\", \"last_name\":\"%s\", \"birth_date\":\"%s\", \"gender\":\"%s\", \"height_feet\":%d, \"height_inches\":%d}",
//                first_name, last_name, birth_date, gender, height_feet, height_inches
//        );
//
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("Response Code: " + responseCode);
//
//            if (responseCode == HttpURLConnection.HTTP_CREATED) {
//                try (Scanner scanner = new Scanner(conn.getInputStream())) {
//                    while (scanner.hasNext()) {
//                        System.out.println(scanner.nextLine());
//                    }
//                }
//            } else {
//                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
//                    while (scanner.hasNext()) {
//                        System.out.println(scanner.nextLine());
//                    }
//                }
//            }
//            return responseCode == 201;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // Login and get JWT token
//    public static String loginUser(String email, String password) throws IOException {
//        String endpoint = DB_URL + "/auth/v1/token?grant_type=password";
//        URL url = new URL(endpoint);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("apikey", DB_KEY);
//        conn.setDoOutput(true);
//
//        String jsonInputString = String.format(
//                "{\"email\": \"%s\", \"password\": \"%s\"}",
//                email, password
//        );
//
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // The response contains the JWT token
//                Scanner scanner = new Scanner(conn.getInputStream());
//                String response = scanner.useDelimiter("\\A").next();
//                System.out.println("Login successful, JWT token: " + response);
//                return response; // Return the JWT token
//            } else {
//                throw new IOException("Login failed, Response Code: " + responseCode);
//            }
//
//        }
//    }
//}

//        if(input.equals("yes")) {
//            User user1 = new User("Sally", "Doe", "1990-04-25", "Female", 5, 10);
//            UserAccount userAccount1 = new UserAccount(user1, "john.doe@example.com", "password123", "johndoe", "123-456-7890");
//            System.out.println("Account created! Thanks a lot, " + user1.getFirst_name() + "!");
//            return user1;
//        }

package org.aimacrosapp;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AccountLogic {
    //dotenv allows me to save keys in env file
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");
//    private static final String SESSION_FILE = "user_session.txt"; // File to store session data
//    private static final String CREDENTIALS_FILE = "credentials.txt";

    //method to establish HTTP connections (user insertion only)
    public HttpURLConnection establishDatabaseConnection(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches, int weight_lbs) throws IOException {
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

    //overloaded method to establish HTTP connections (user update insertion only)
    public HttpURLConnection establishDatabaseConnection(UUID user_id, String field, String newValue) throws IOException {
        URL url = new URL(DB_URL + "/rest/v1/users?user_id=eq." + user_id);
        //open HTTP connection for API call
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //connecting to supabase via API request to post data to user account table
        conn.setRequestMethod("PATCH");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=minimal");
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

    //method to update/edit a user field
    public void updateUserField(UUID userId) {
        Scanner scanner = new Scanner(System.in);
        // List of valid fields
        String[] validFields = {"first_name", "last_name", "birth_date", "gender", "height_feet", "height_inches", "weight_lbs", "body_type", "experience_level", "activity_level", "primary_goal"};

        System.out.println("Which field do you want to update?");
        String fieldToUpdate = scanner.nextLine().trim().toLowerCase();

        // Validate field
        boolean isValidField = false;
        for (String field : validFields) {
            if (field.equals(fieldToUpdate)) {
                isValidField = true;
                break;
            }
        }

        if (!isValidField) {
            System.out.println("Invalid field name.");
            return;
        }

        System.out.println("Enter the new value:");
        String newValue = scanner.nextLine().trim();

        // Call API update method
        updateDatabase(userId, fieldToUpdate, newValue);
    }

    public void updateDatabase(UUID userId, String field, String newValue) {
        try {
            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Construct the PATCH request body
            String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

            // Create HTTP PATCH request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DB_URL + "/rest/v1/users?user_id=eq." + userId))
                    .header("Content-Type", "application/json")
                    .header("apikey", DB_KEY)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                System.out.println("Update successful!");
            } else {
                System.out.println("Update failed: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database update failed.");
        }
    }

    //method to delete user from user table
    public static void deleteUser(UUID user_id) throws Exception {
        URI uri = new URI(DB_URL + "/rest/v1/users?user_id=eq." + user_id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("apikey", DB_KEY)  // Supabase requires the API key
                .header("Authorization", "Bearer " + DB_KEY) // Sometimes needed
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.noBody()) // DELETE request
                .build();

        HttpResponse<Void> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() == 204) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user. Status code: " + response.statusCode());
        }
    }

    //method to update/edit a user_account field
    public void updateUserAccountField(UUID user_account_id) {
        Scanner scanner = new Scanner(System.in);
        // List of valid fields
        String[] validFields = {"email", "password", "nickname", "phone_number", "email_second"};

        System.out.println("Which field do you want to update?");
        String fieldToUpdate = scanner.nextLine().trim().toLowerCase();

        // Validate field
        boolean isValidField = false;
        for (String field : validFields) {
            if (field.equals(fieldToUpdate)) {
                isValidField = true;
                break;
            }
        }

        if (!isValidField) {
            System.out.println("Invalid field name.");
            return;
        }

        System.out.println("Enter the new value:");
        String newValue = scanner.nextLine().trim();

        // Call API update method
        updateDatabaseAccount(user_account_id, fieldToUpdate, newValue);
    }

    //method to update user account table
    public void updateDatabaseAccount(UUID user_account_id, String field, String newValue) {
        try {
            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Construct the PATCH request body
            String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

            // Create HTTP PATCH request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DB_URL + "/rest/v1/user_account?user_account_id=eq." + user_account_id))
                    .header("Content-Type", "application/json")
                    .header("apikey", DB_KEY)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                System.out.println("Update successful!");
            } else {
                System.out.println("Update failed: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database update failed.");
        }
    }

    // Method to create a new user and insert into the database (bypassing authentication)
    public String createUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches, int weight_lbs,
                             String body_type, String experience_level, String activity_level, String primary_goal) throws IOException {
        // Prepare JSON input for insertion into user table
        String jsonInputString = String.format(
                "{\"first_name\":\"%s\", \"last_name\":\"%s\", \"birth_date\":\"%s\", \"gender\":\"%s\", " +
                        "\"height_feet\":%d, \"height_inches\":%d, \"weight_lbs\":%d, " +
                        "\"body_type\":\"%s\", \"experience_level\":\"%s\", \"activity_level\":\"%s\", \"primary_goal\":\"%s\"}",
                first_name, last_name, birth_date, gender, height_feet, height_inches, weight_lbs,
                body_type, experience_level, activity_level, primary_goal
        );

        //call to HTTP connection
        HttpURLConnection conn = establishDatabaseConnection(first_name, last_name, birth_date, gender, height_feet, height_inches, weight_lbs);

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

    // Method to sign in a user
    public boolean signIn(String email, String password) {
        try {
            // Encode email to be URL-safe
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

            // Construct API request (adjust table name accordingly)
            String requestUrl = DB_URL + "/rest/v1/user_account?email=eq." + encodedEmail;

            // Print the URL for debugging
            System.out.println("Requesting: " + requestUrl);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("apikey", DB_KEY)
                    .header("Authorization", "Bearer " + DB_KEY)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print response status and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                if (jsonArray.length() == 0) {
                    System.out.println("User not found.");
                    return false;
                }

                JSONObject userObject = jsonArray.getJSONObject(0);
                String storedPassword = userObject.getString("password"); // Assuming plaintext, but should be hashed!

                // Compare password (ideally, compare hashed values)
                if (password.equals(storedPassword)) {
                    System.out.println("Sign-in successful!");
                    return true;
                } else {
                    System.out.println("Incorrect password.");
                    return false;
                }
            } else {
                System.out.println("Sign-in failed: " + response.statusCode());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verification questions method before resetting password
    public boolean confirmAccount(String email, String nickname) {
        try {
            // Encode email to be URL-safe
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

            // Construct API request (adjust table name accordingly)
            String requestUrl = DB_URL + "/rest/v1/user_account?email=eq." + encodedEmail + "&nickname=eq." + URLEncoder.encode(nickname, StandardCharsets.UTF_8);

            // Print the URL for debugging
            System.out.println("Requesting: " + requestUrl);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("apikey", DB_KEY)
                    .header("Authorization", "Bearer " + DB_KEY)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print response status and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            System.out.println("Encoded Email: " + encodedEmail);
            System.out.println("Encoded Nickname: " + URLEncoder.encode(nickname, StandardCharsets.UTF_8));


            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                if (jsonArray.length() == 0) {
                    System.out.println("User not found.");
                    return false;
                }

                JSONObject userObject = jsonArray.getJSONObject(0);
                String storedNickname = userObject.getString("nickname");

                // Compare password (ideally, compare hashed values)
                if (nickname.equals(storedNickname)) {
                    System.out.println("successful!");
                    return true;
                } else {
                    System.out.println("Incorrect.");
                    return false;
                }
            } else {
                System.out.println("failed: " + response.statusCode());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //setup connection to database for getUserAccountByEmail
    private HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
        URL url = new URL(DB_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY);
        conn.setDoOutput(true); // optional for GET
        return conn;
    }

    //get user account for session
    public UserAccount getUserAndAccountByEmail(String email) throws IOException {
        // Step 1: Fetch user_account row by email
        String endpoint = "/rest/v1/user_account?email=eq." + email + "&select=*";
        HttpURLConnection conn = setupConnection(endpoint, "GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }

                JSONArray jsonArray = new JSONArray(jsonResponse.toString());
                if (jsonArray.length() > 0) {
                    JSONObject accountObj = jsonArray.getJSONObject(0);

                    // Step 2: Parse user_account fields
                    UserAccount account = new UserAccount();
                    account.setEmail(accountObj.getString("email"));
                    account.setPassword(accountObj.getString("password"));
                    account.setNickname(accountObj.optString("nickname", null));
                    account.setPhone_number(accountObj.optString("phone_number", null));
                    account.setEmail_second(accountObj.optString("email_second", null));

                    String userId = accountObj.getString("user_id");

                    // Step 3: Fetch corresponding user row
                    String userEndpoint = "/rest/v1/user?user_id=eq." + userId + "&select=*";
                    HttpURLConnection userConn = setupConnection(userEndpoint, "GET");

                    int userResponseCode = userConn.getResponseCode();
                    if (userResponseCode == HttpURLConnection.HTTP_OK) {
                        try (Scanner userScanner = new Scanner(userConn.getInputStream())) {
                            StringBuilder userJson = new StringBuilder();
                            while (userScanner.hasNext()) {
                                userJson.append(userScanner.nextLine());
                            }

                            JSONArray userArray = new JSONArray(userJson.toString());
                            if (userArray.length() > 0) {
                                JSONObject userObj = userArray.getJSONObject(0);
                                User user = new User();
                                user.setFirst_name(userObj.optString("first_name"));
                                user.setLast_name(userObj.optString("last_name"));
                                user.setBirth_date(userObj.optString("birth_date"));
                                user.setGender(userObj.optString("gender"));
                                user.setHeight_feet(userObj.optInt("height_feet"));
                                user.setHeight_inches(userObj.optInt("height_inches"));
                                user.setWeight_lbs(userObj.optInt("weight_lbs"));
                                user.setBody_type(userObj.optString("body_type", null));
                                user.setExperience_level(userObj.optString("experience_level", null));
                                user.setActivity_level(userObj.optString("activity_level", null));
                                user.setPrimary_goal(userObj.optString("primary_goal", null));

                                // Step 4: Attach user to account
                                account.setUser(user);
                            }
                        }
                    }

                    return account;
                }
            }
        }

        System.out.println("Failed to retrieve UserAccount info for: " + email);
        return null;
    }


    // Method to reset password after verification
//    public void updatePassword(String email, String password, String confirmPassword) {
//
//
//        //extract user id?
//
//
//        // Validate field
//        boolean isMatch = false;
//            if (password.equals(confirmPassword)) {
//                isMatch = true;
//            }
//
//        if (!isMatch) {
//            System.out.println("Field names do not match.");
//            return;
//        }
//
//        // Call API update method
//        updateDatabase(userId, password, confirmPassword);
//    }

    // Method to update multiple user_account fields based on changes in the GUI
//    public void updateUserAccountFields(UUID user_account_id, Map<String, String> updatedFields) {
//        try {
//            // Create HTTP client
//            HttpClient client = HttpClient.newHttpClient();
//
//            // Construct the PATCH request body dynamically
//            StringBuilder jsonInputStringBuilder = new StringBuilder("{");
//
//            // Loop through the updated fields and append them to the request body
//            boolean first = true;
//            for (Map.Entry<String, String> entry : updatedFields.entrySet()) {
//                if (!first) {
//                    jsonInputStringBuilder.append(", ");
//                }
//                jsonInputStringBuilder.append("\"")
//                        .append(entry.getKey())
//                        .append("\": \"")
//                        .append(entry.getValue())
//                        .append("\"");
//                first = false;
//            }
//
//            // Close the JSON object
//            jsonInputStringBuilder.append("}");
//
//            // Create the PATCH request
//            String jsonInputString = jsonInputStringBuilder.toString();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(DB_URL + "/rest/v1/user_account?user_account_id=eq." + user_account_id))
//                    .header("Content-Type", "application/json")
//                    .header("apikey", DB_KEY)
//                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
//                    .build();
//
//            // Send request and get response
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200 || response.statusCode() == 204) {
//                System.out.println("Update successful!");
//            } else {
//                System.out.println("Update failed: " + response.statusCode());
//                System.out.println(response.body());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Database update failed.");
//        }
//    }

    // Example method to get updated fields from GUI text fields
//    public Map<String, String> getUpdatedFieldsFromGUI() {
//        Map<String, String> updatedFields = new HashMap<>();
//
//        // Get current values from the text fields in the GUI
//        String newEmail = emailTextField.getText().trim();
//        String newPassword = passwordTextField.getText().trim();
//        String newNickname = nicknameTextField.getText().trim();
//        String newPhoneNumber = phoneNumberTextField.getText().trim();
//        String newEmailSecond = emailSecondTextField.getText().trim();
//
//        // Compare each field with the original value and add to the map if changed
//        if (!newEmail.equals(originalEmail)) {
//            updatedFields.put("email", newEmail);
//        }
//        if (!newPassword.equals(originalPassword)) {
//            updatedFields.put("password", newPassword);
//        }
//        if (!newNickname.equals(originalNickname)) {
//            updatedFields.put("nickname", newNickname);
//        }
//        if (!newPhoneNumber.equals(originalPhoneNumber)) {
//            updatedFields.put("phone_number", newPhoneNumber);
//        }
//        if (!newEmailSecond.equals(originalEmailSecond)) {
//            updatedFields.put("email_second", newEmailSecond);
//        }
//
//        return updatedFields;
//    }



//    // Method to start a user session (save user ID)
//    public void startUserSession(String userId) {
//        try (FileWriter fileWriter = new FileWriter(SESSION_FILE)) {
//            fileWriter.write(userId);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to check if a user session exists (returns user ID or null)
//    public String getUserSession() {
//        File file = new File(SESSION_FILE);
//        if (file.exists()) {
//            try (Scanner scanner = new Scanner(file)) {
//                return scanner.nextLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    // Method to end the user session (log out)
//    public void endUserSession() {
//        File file = new File(SESSION_FILE);
//        if (file.exists()) {
//            file.delete();
//        }
//        System.out.println("User signed out.");
//    }


}
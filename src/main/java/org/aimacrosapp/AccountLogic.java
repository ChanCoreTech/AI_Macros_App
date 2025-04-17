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
import org.mindrot.jbcrypt.BCrypt;

public class AccountLogic {
    //dotenv allows me to save keys in env file
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");

    // Hash password
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Check password
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    //method to establish HTTP connections (user insertion only)
    public HttpURLConnection establishDatabaseConnection(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches, int weight_lbs) throws IOException {
        // Endpoint for user insertion
        String endpoint = DB_URL + "/rest/v1/users";
        URL url = new URL(endpoint);

        // Open HTTP connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setRequestProperty("apikey", DB_KEY);

        // Include bearer token for authorization
        String accessToken = Session.getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

        conn.setDoOutput(true);
        return conn;
    }

    //overloaded method to establish HTTP connections (user account insertion only)
    public HttpURLConnection establishDatabaseConnection(String email, String password, String nickname, String phone_number, String email_second) throws IOException {
        // Endpoint for inserting into user_account table
        String endpoint = DB_URL + "/rest/v1/user_account";
        URL url = new URL(endpoint);

        // Open HTTP connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setRequestProperty("apikey", DB_KEY);

        // ✅ Include bearer token from signed-in session
        String accessToken = Session.getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

        conn.setDoOutput(true);
        return conn;
    }

    //overloaded method to establish HTTP connections (user update insertion only)
    public HttpURLConnection establishDatabaseConnection(UUID user_id, String field, String newValue) throws IOException {
        URL url = new URL(DB_URL + "/rest/v1/users?user_id=eq." + user_id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PATCH");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=minimal");
        conn.setRequestProperty("apikey", DB_KEY);

        // ✅ Include the access token for authenticated access
        String accessToken = Session.getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

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

    // Method to get user id from email
    public String extractUserIdFromEmail(String email) throws IOException {
        String accessToken = Session.getAccessToken();  // ← Get token from session
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token is missing. User may not be authenticated.");
            return null;
        }

        String endpoint = DB_URL + "/rest/v1/user_account?email=eq." + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&select=user_id";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + accessToken)
                .header("apikey", DB_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                if (jsonArray.length() > 0) {
                    return jsonArray.getJSONObject(0).getString("user_id");
                }
            } else {
                System.out.println("Failed to fetch user_id. Status: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println("Failed to extract user_id for email: " + email);
        return null;
    }


    public UserAccount getUserAccountForSession() throws IOException {
        String email = Session.getEmail();
        return getUserAndAccountByEmail(email); // this already returns both user + user_account
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
            String accessToken = Session.getAccessToken();  // Ensure token is available
            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("Access token missing. Cannot perform update.");
                return;
            }

            HttpClient client = HttpClient.newHttpClient();

            // Construct JSON PATCH body
            String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

            // Build request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DB_URL + "/rest/v1/users?user_id=eq." + userId))
                    .header("Authorization", "Bearer " + accessToken)  // 🔐 Auth for RLS
                    .header("apikey", DB_KEY)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

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
            String accessToken = Session.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("Access token missing. Cannot perform account update.");
                return;
            }

            HttpClient client = HttpClient.newHttpClient();

            String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DB_URL + "/rest/v1/user_account?user_account_id=eq." + user_account_id))
                    .header("Authorization", "Bearer " + accessToken)  // 🔐 Auth
                    .header("apikey", DB_KEY)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                System.out.println("User account update successful!");
            } else {
                System.out.println("User account update failed: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Account update failed due to exception.");
        }
    }

    public String signUpUser(String email, String plainPassword) throws IOException, InterruptedException {
        String signupUrl = DB_URL + "/auth/v1/signup";

        String requestBody = String.format(
                "{\"email\":\"%s\", \"password\":\"%s\"}",
                email, plainPassword
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(signupUrl))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Signing up with: [" + email + "]");
        System.out.println("Sign-up JSON: " + requestBody);

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            JSONObject json = new JSONObject(response.body());
            String accessToken = json.getString("access_token");
            String userId = json.getJSONObject("user").getString("id");

            Session.setAccessToken(accessToken);
            return userId;
        } else {
            System.out.println("Sign-up failed: " + response.body());
            return null;
        }
    }

    // Method to create a new user and insert into the database
    public String createUser(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches, int weight_lbs,
                             String body_type, String experience_level, String activity_level, String primary_goal) throws IOException {
        String accessToken = Session.getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token missing. Cannot create user.");
            return null;
        }

        String jsonInputString = String.format(
                "{\"first_name\":\"%s\", \"last_name\":\"%s\", \"birth_date\":\"%s\", \"gender\":\"%s\", " +
                        "\"height_feet\":%d, \"height_inches\":%d, \"weight_lbs\":%d, " +
                        "\"body_type\":\"%s\", \"experience_level\":\"%s\", \"activity_level\":\"%s\", \"primary_goal\":\"%s\"}",
                first_name, last_name, birth_date, gender, height_feet, height_inches, weight_lbs,
                body_type, experience_level, activity_level, primary_goal
        );

        HttpURLConnection conn = establishDatabaseConnection(first_name, last_name, birth_date, gender, height_feet, height_inches, weight_lbs);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
            int responseCode = conn.getResponseCode();
            System.out.println("Sending JSON: " + jsonInputString);

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                    System.out.println("Raw JSON response: " + jsonResponse.toString());
                    return extractUserId(jsonResponse.toString());
                }
            } else {
                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // Method to create a new user account and insert into the database
    public boolean createUserAccount(String email, String password, String nickname,
                                     String phone_number, String email_second, String user_id) throws IOException {
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        String accessToken = Session.getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token missing. Cannot create user account.");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{")
                .append("\"email\":\"").append(email).append("\",")
                .append("\"password\":\"").append(hashedPassword).append("\",")
                .append("\"nickname\":\"").append(nickname).append("\",")
                .append("\"phone_number\":\"").append(phone_number).append("\",")
                .append("\"user_id\":\"").append(user_id).append("\"");

        if (email_second != null && !email_second.trim().isEmpty()) {
            jsonBuilder.append(",\"email_second\":\"").append(email_second).append("\"");
        }

        jsonBuilder.append("}");

        HttpURLConnection conn = establishDatabaseConnection(email, password, nickname, phone_number, email_second);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBuilder.toString().getBytes(StandardCharsets.UTF_8));
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) {
                        System.out.println(scanner.nextLine());
                    }
                }
                return true;
            } else {
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
            return false;
        }
    }



    //methods to update/edit user accounts
    public boolean updateUser(String email, String firstName, String lastName, String birthDate, String gender,
                              int heightFeet, int heightInches, int weightLbs, String bodyType,
                              String experienceLevel, String activityLevel, String primaryGoal) throws IOException, InterruptedException {

        String userId = extractUserIdFromEmail(email);
        if (userId == null) {
            System.out.println("Could not find user_id for email: " + email);
            return false;
        }

        String accessToken = Session.getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token missing. Cannot update user.");
            return false;
        }

        JSONObject json = new JSONObject();
        json.put("first_name", firstName);
        json.put("last_name", lastName);
        json.put("birth_date", birthDate);
        json.put("gender", gender);
        json.put("height_feet", heightFeet);
        json.put("height_inches", heightInches);
        json.put("weight_lbs", weightLbs);

        if (bodyType != null && !bodyType.isEmpty()) json.put("body_type", bodyType);
        if (experienceLevel != null && !experienceLevel.isEmpty()) json.put("experience_level", experienceLevel);
        if (activityLevel != null && !activityLevel.isEmpty()) json.put("activity_level", activityLevel);
        if (primaryGoal != null && !primaryGoal.isEmpty()) json.put("primary_goal", primaryGoal);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DB_URL + "/rest/v1/users?user_id=eq." + userId))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .header("Authorization", "Bearer " + accessToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("User update response: " + response.body());
        return response.statusCode() == 204 || response.statusCode() == 200;
    }


    //method to update/edit user account
    public boolean updateUserAccount(String email, String password, String nickname,
                                     String phoneNumber, String emailSecond) throws IOException, InterruptedException {

        String accessToken = Session.getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token missing. Cannot update user account.");
            return false;
        }

        JSONObject json = new JSONObject();

        if (password != null && !password.isEmpty()) json.put("password", password);
        if (nickname != null && !nickname.isEmpty()) json.put("nickname", nickname);
        if (phoneNumber != null && !phoneNumber.isEmpty()) json.put("phone_number", phoneNumber);
        if (emailSecond != null && !emailSecond.isEmpty()) json.put("email_second", emailSecond);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DB_URL + "/rest/v1/user_account?email=eq." + URLEncoder.encode(email, StandardCharsets.UTF_8)))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .header("Authorization", "Bearer " + accessToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("UserAccount update response: " + response.body());
        return response.statusCode() == 204 || response.statusCode() == 200;
    }


    // Method to sign in a user
    public boolean signIn(String email, String plainPassword) {
        try {
            // Step 1: Sign in using Supabase Auth REST API to get a JWT access token
            String authUrl = DB_URL + "/auth/v1/token?grant_type=password";  // Update if needed

            // Construct JSON body for sign-in
            String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, plainPassword);

            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(URI.create(authUrl))
                    .header("apikey", DB_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

            if (authResponse.statusCode() != 200) {
                System.out.println("Authentication failed: " + authResponse.body());
                return false;
            }

            // Step 2: Parse token from JSON response
            JSONObject authJson = new JSONObject(authResponse.body());
            String accessToken = authJson.getString("access_token");

            // Optional: Store token for session use
            Session.setAccessToken(accessToken);

            // Step 3: Use the token to fetch user account info securely
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String userRequestUrl = DB_URL + "/rest/v1/user_account?email=eq." + encodedEmail;

            HttpRequest userRequest = HttpRequest.newBuilder()
                    .uri(URI.create(userRequestUrl))
                    .header("Authorization", "Bearer " + accessToken)  // <- Use the token here
                    .header("apikey", DB_KEY)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());

            if (userResponse.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(userResponse.body());
                if (jsonArray.length() == 0) return false;

                JSONObject userObject = jsonArray.getJSONObject(0);
                String hashedPassword = userObject.getString("password");

                // Step 4: Double-check password hash if needed
                return checkPassword(plainPassword, hashedPassword);
            } else {
                System.out.println("Error fetching user data: " + userResponse.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean confirmAccount(String email, String nickname) {
        try {
            String accessToken = Session.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("Access token missing. Cannot confirm account.");
                return false;
            }

            // Encode query parameters
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
            String requestUrl = DB_URL + "/rest/v1/user_account?email=eq." + encodedEmail + "&nickname=eq." + encodedNickname;

            System.out.println("Requesting: " + requestUrl);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("Content-Type", "application/json")
                    .header("apikey", DB_KEY)
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                if (jsonArray.length() == 0) {
                    System.out.println("User not found.");
                    return false;
                }

                JSONObject userObject = jsonArray.getJSONObject(0);
                String storedNickname = userObject.getString("nickname");

                if (nickname.equals(storedNickname)) {
                    System.out.println("Successful match.");
                    return true;
                } else {
                    System.out.println("Nickname does not match.");
                    return false;
                }
            } else {
                System.out.println("Failed: " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updatePassword(String email, String newPassword, String confirmPassword) {
        if (email == null || email.isEmpty()) {
            System.out.println("Email is required.");
            return false;
        }

        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match or are empty.");
            return false;
        }

        try {
            // Get the token from session
            String accessToken = Session.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("Access token missing. Cannot update password.");
                return false;
            }

            // Hash the new password using BCrypt
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            // Build the PATCH body with the hashed password
            JSONObject json = new JSONObject();
            json.put("password", hashedPassword);

            // PATCH request to update password securely
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DB_URL + "/rest/v1/user_account?email=eq." + URLEncoder.encode(email, StandardCharsets.UTF_8)))
                    .header("Content-Type", "application/json")
                    .header("apikey", DB_KEY)
                    .header("Authorization", "Bearer " + accessToken) // ✅ Use user's token
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.statusCode() == 200) {
                System.out.println("Password updated successfully for: " + email);
                return true;
            } else {
                System.out.println("Failed to update password: " + response.body());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    //setup connection to database for getUserAccountByEmail
    private HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
        String accessToken = Session.getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IOException("Access token is missing. User must be signed in.");
        }

        URL url = new URL(DB_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken); // ✅ Use token
        conn.setDoOutput(true); // optional for GET
        return conn;
    }


    //get user account for session
    public UserAccount getUserAndAccountByEmail(String email) throws IOException {
        email = email.trim();
        String endpoint = "/rest/v1/user_account?email=eq." + email + "&select=*";
        HttpURLConnection conn = setupConnection(endpoint, "GET");

        System.out.println("Querying email: [" + email + "]");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }
                System.out.println("Raw Supabase response: " + jsonResponse.toString());

                JSONArray jsonArray = new JSONArray(jsonResponse.toString());
                if (jsonArray.length() > 0) {
                    JSONObject accountObj = jsonArray.getJSONObject(0);

                    // Parse user_account fields
                    UserAccount account = new UserAccount();
                    account.setEmail(accountObj.getString("email"));
                    account.setPassword(accountObj.getString("password"));
                    account.setNickname(accountObj.optString("nickname", null));
                    account.setPhone_number(accountObj.optString("phone_number", null));
                    account.setEmail_second(accountObj.optString("email_second", null));

                    String userId = accountObj.getString("user_id");

                    // Fetch corresponding user record
                    String userEndpoint = "/rest/v1/users?user_id=eq." + userId + "&select=*";
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

                                // Attach to account
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
}


package org.aimacrosapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GoalsLogic {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");

    private String cachedUserId = null;

    private HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
        URL url = new URL(DB_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);  // Ensure method is properly set

        // Some APIs require handling PUT instead of PATCH, so you can try using PUT as an alternative
        if (method.equals("PATCH") || method.equals("PUT")) {
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        }

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY);
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setDoOutput(true);

        return conn;
    }



    private String extractUserId(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject.getString("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserIdByEmail(String email) throws IOException {
        String endpoint = "/rest/v1/user_account?email=eq." + email + "&select=user_id";
        HttpURLConnection conn = setupConnection(endpoint, "GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }
                return extractUserId(jsonResponse.toString());
            }
        } else {
            System.out.println("Failed to fetch user_id with email: " + email);
            return null;
        }
    }


    public String getUserId(String email) throws IOException {
        if (cachedUserId == null) {
            cachedUserId = getUserIdByEmail(email);
        }
        return cachedUserId;
    }

    public boolean checkIfUserGoalsExist(String user_id) throws IOException {
        String endpoint = "/rest/v1/user_goals?user_id=eq." + user_id;
        HttpURLConnection conn = setupConnection(endpoint, "GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }
                JSONArray resultArray = new JSONArray(jsonResponse.toString());
                return resultArray.length() > 0;
            }
        }
        return false;
    }

    public boolean upsertGoals(String workouts_per_week, String daily_calories, String daily_carbs,
                               String daily_protein, String daily_fats, String email) throws IOException, InterruptedException {
        String user_id = getUserId(email);
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        // List of fields to update
        String[] fields = {"workouts_per_week", "daily_calories", "daily_carbs", "daily_protein", "daily_fats"};
        String[] values = {workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats};

        // Loop through each field and attempt to update it individually
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String value = values[i];

            // Call update method for each field
            updateUserGoalField(user_id, field, value);
        }

        return true;
    }

    public void updateUserGoalField(String user_id, String field, String newValue) throws IOException, InterruptedException {
        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Construct the PATCH request body
        String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

        // Create HTTP PATCH request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DB_URL + "/rest/v1/user_goals?user_id=eq." + user_id))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            System.out.println("Update successful for field: " + field);
        } else {
            System.out.println("Update failed for field: " + field);
            System.out.println(response.body());
        }
    }


    public boolean createGoalHistory(String goal_date, String todays_workout, String todays_calories,
                                     String todays_carbs, String todays_protein, String todays_fats,
                                     String email) throws IOException {
        String user_id = getUserId(email);
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        String jsonInputString = String.format(
                "{\"goal_date\":\"%s\", \"todays_workout\":\"%s\", \"todays_calories\":\"%s\", " +
                        "\"todays_carbs\":\"%s\", \"todays_protein\":\"%s\", \"todays_fats\":\"%s\", \"user_id\":\"%s\"}",
                goal_date, todays_workout, todays_calories, todays_carbs, todays_protein, todays_fats, user_id
        );

        HttpURLConnection conn = setupConnection("/rest/v1/user_goal_history", "POST");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Creating user_goal_history entry with JSON: " + jsonInputString);

        try (Scanner scanner = new Scanner(
                responseCode == HttpURLConnection.HTTP_CREATED ? conn.getInputStream() : conn.getErrorStream()
        )) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            System.out.println("Response: " + response.toString());
        }

        return responseCode == HttpURLConnection.HTTP_CREATED;
    }
}

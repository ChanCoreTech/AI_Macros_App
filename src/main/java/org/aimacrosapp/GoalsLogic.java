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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GoalsLogic {
    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env")
            .load();

    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");

    //user id cached
    private String cachedUserId = null;

    //method for setting up HTTP connection
    private HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
        URL url = new URL(DB_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("apikey", DB_KEY);

        // Add Authorization header using token stored in Session
        String token = Session.getAccessToken();
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        conn.setRequestProperty("Prefer", "return=representation");
        conn.setDoOutput(true); // optional for GET, required for POST/PATCH/DELETE

        return conn;
    }

    //method to get user id
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

    //method to get user id
    public String getUserIdByEmail(String email) throws IOException {
        String endpoint = "/rest/v1/user_account?email=eq." + email + "&select=user_id";

        HttpURLConnection conn = setupConnection(endpoint, "GET");

        // Add Authorization token to the request
        String token = Session.getAccessToken();
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

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

    //method to get user id
    public String getUserId(String email) throws IOException {
        if (cachedUserId == null) {
            cachedUserId = getUserIdByEmail(email);
        }
        return cachedUserId;
    }

    //update or insert goals method
    public boolean upsertGoals(String workouts_per_week, String daily_calories, String daily_carbs,
                               String daily_protein, String daily_fats, String email) throws IOException, InterruptedException {
        String user_id = getUserId(email);
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        if (!userGoalsExist(user_id)) {
            // No row yet, insert a new one
            return createUserGoals(user_id, workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats);
        }

        // Otherwise, update fields one by one
        String[] fields = {"workouts_per_week", "daily_calories", "daily_carbs", "daily_protein", "daily_fats"};
        String[] values = {workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats};

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String value = values[i];
            updateUserGoalField(user_id, field, value); // make sure this uses the token
        }

        return true;
    }

    //method to create user goals
    private boolean createUserGoals(String user_id, String workouts_per_week, String daily_calories,
                                    String daily_carbs, String daily_protein, String daily_fats) throws IOException {
        String jsonInputString = String.format(
                "{\"user_id\":\"%s\", \"workouts_per_week\":\"%s\", \"daily_calories\":%s, " +
                        "\"daily_carbs\":%s, \"daily_protein\":%s, \"daily_fats\":%s}",
                user_id, workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats
        );

        HttpURLConnection conn = setupConnection("/rest/v1/user_goals", "POST");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Creating user_goals entry with JSON: " + jsonInputString);

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

    //method to differentiate if goals are empty or not for upsertGoals()
    private boolean userGoalsExist(String user_id) throws IOException, InterruptedException {
        String endpoint = DB_URL + "/rest/v1/user_goals?user_id=eq." + user_id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .header("Authorization", "Bearer " + Session.getAccessToken())
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray resultArray = new JSONArray(response.body());
            return resultArray.length() > 0;
        } else {
            System.out.println("Failed to check for user_goals entry. Status: " + response.statusCode());
            System.out.println("Response: " + response.body());
            return false;
        }
    }

    //method to update user goals
    public void updateUserGoalField(String user_id, String field, String newValue) throws IOException, InterruptedException {
        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Construct the PATCH request body
        String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

        // Create HTTP PATCH request with access token
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DB_URL + "/rest/v1/user_goals?user_id=eq." + user_id))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .header("Authorization", "Bearer " + Session.getAccessToken())
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            System.out.println("Update successful for field: " + field);
        } else {
            System.out.println("Update failed for field: " + field);
            System.out.println("Status: " + response.statusCode());
            System.out.println("Response: " + response.body());
        }
    }

    //method to update goal history
    private void updateGoalHistoryField(String user_id, String goal_date, String field, String newValue) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String jsonInputString = String.format("{\"%s\":\"%s\"}", field, newValue);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DB_URL + "/rest/v1/user_goal_history?user_id=eq." + user_id + "&goal_date=eq." + goal_date))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .header("Authorization", "Bearer " + Session.getAccessToken())
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            System.out.println("Updated field in goal history: " + field);
        } else {
            System.out.println("Failed to update field: " + field);
            System.out.println("Status: " + response.statusCode());
            System.out.println("Response: " + response.body());
        }
    }

    //update or insert into goal history
    public boolean upsertGoalHistory(String goal_date, String todays_workout, String todays_calories,
                                     String todays_carbs, String todays_protein, String todays_fats,
                                     String email) throws IOException, InterruptedException {
        String user_id = getUserId(email);
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        if (goalHistoryExists(user_id, goal_date)) {
            // Update fields
            updateGoalHistoryField(user_id, goal_date, "todays_workout", todays_workout);
            updateGoalHistoryField(user_id, goal_date, "todays_calories", todays_calories);
            updateGoalHistoryField(user_id, goal_date, "todays_carbs", todays_carbs);
            updateGoalHistoryField(user_id, goal_date, "todays_protein", todays_protein);
            updateGoalHistoryField(user_id, goal_date, "todays_fats", todays_fats);
        } else {
            // Insert new entry
            return createGoalHistory(goal_date, todays_workout, todays_calories, todays_carbs, todays_protein, todays_fats, email);
        }

        return true;
    }

    //insert goal history into DB
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

        // === Build request ===
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DB_URL + "/rest/v1/user_goal_history"))
                .header("Content-Type", "application/json")
                .header("apikey", DB_KEY)
                .header("Authorization", "Bearer " + Session.getAccessToken())  // Use token!
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        // request
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Creating user_goal_history entry with JSON: " + jsonInputString);
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        return response.statusCode() == 201;  // 201 = HTTP_CREATED
    }

    //method to check go history status for upsert
    private boolean goalHistoryExists(String user_id, String goal_date) throws IOException {
        String endpoint = "/rest/v1/user_goal_history?user_id=eq." + user_id + "&goal_date=eq." + goal_date;
        HttpURLConnection conn = setupConnection(endpoint, "GET");

        conn.setRequestProperty("Authorization", "Bearer " + Session.getAccessToken());

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
        } else {
            System.out.println("Failed to check for goal history entry");
            return false;
        }
    }

    //get goals and history by email
    public UserGoalData getUserGoalsAndHistoryByEmail(String email) throws IOException {
        // Fetch user_id from user_account using email
        String accountEndpoint = "/rest/v1/user_account?email=eq." + email + "&select=user_id";
        HttpURLConnection accountConn = setupConnection(accountEndpoint, "GET");
        // Add Authorization token to the request
        accountConn.setRequestProperty("Authorization", "Bearer " + Session.getAccessToken());

        int accountResponse = accountConn.getResponseCode();
        if (accountResponse == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(accountConn.getInputStream())) {
                StringBuilder responseBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    responseBuilder.append(scanner.nextLine());
                }

                JSONArray accountArray = new JSONArray(responseBuilder.toString());
                if (accountArray.length() > 0) {
                    String userId = accountArray.getJSONObject(0).getString("user_id");

                    // Fetch user_goals by user_id
                    String goalsEndpoint = "/rest/v1/user_goals?user_id=eq." + userId + "&select=*";
                    HttpURLConnection goalsConn = setupConnection(goalsEndpoint, "GET");
                    // Add Authorization token to the request
                    goalsConn.setRequestProperty("Authorization", "Bearer " + Session.getAccessToken());

                    UserGoals userGoals = null;
                    if (goalsConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        try (Scanner gScanner = new Scanner(goalsConn.getInputStream())) {
                            StringBuilder gBuilder = new StringBuilder();
                            while (gScanner.hasNext()) {
                                gBuilder.append(gScanner.nextLine());
                            }

                            JSONArray gArray = new JSONArray(gBuilder.toString());
                            if (gArray.length() > 0) {
                                JSONObject g = gArray.getJSONObject(0);
                                userGoals = new UserGoals();
                                userGoals.setWorkouts_per_week(g.optString("workouts_per_week", ""));
                                userGoals.setDaily_calories(g.optInt("daily_calories"));
                                userGoals.setDaily_carbs(g.optInt("daily_carbs"));
                                userGoals.setDaily_protein(g.optInt("daily_protein"));
                                userGoals.setDaily_fats(g.optInt("daily_fats"));
                            }
                        }
                    }

                    // Fetch user_goal_history by user_id
                    String historyEndpoint = "/rest/v1/user_goal_history?user_id=eq." + userId + "&select=*";
                    HttpURLConnection historyConn = setupConnection(historyEndpoint, "GET");
                    // Add Authorization token to the request
                    historyConn.setRequestProperty("Authorization", "Bearer " + Session.getAccessToken());

                    List<UserGoalHistory> historyList = new ArrayList<>();
                    if (historyConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        try (Scanner hScanner = new Scanner(historyConn.getInputStream())) {
                            StringBuilder hBuilder = new StringBuilder();
                            while (hScanner.hasNext()) {
                                hBuilder.append(hScanner.nextLine());
                            }

                            JSONArray hArray = new JSONArray(hBuilder.toString());
                            for (int i = 0; i < hArray.length(); i++) {
                                JSONObject h = hArray.getJSONObject(i);
                                UserGoalHistory history = new UserGoalHistory();
                                history.setGoal_date(h.optString("goal_date"));
                                history.setTodays_workout(h.optBoolean("todays_workout", false));
                                history.setTodays_calories(h.optInt("todays_calories"));
                                history.setTodays_carbs(h.optInt("todays_carbs"));
                                history.setTodays_protein(h.optInt("todays_protein"));
                                history.setTodays_fats(h.optInt("todays_fats"));
                                historyList.add(history);
                            }
                        }
                    }

                    return new UserGoalData(userGoals, historyList);
                }
            }
        }

        System.out.println("Failed to retrieve user goal data for: " + email);
        return null;
    }

    //delete goal history row method
    public void deleteGoalHistoryRows(String accessToken, List<String> datesToDelete) throws IOException, InterruptedException {
        for (String goalDate : datesToDelete) {
            String encodedDate = java.net.URLEncoder.encode(goalDate, java.nio.charset.StandardCharsets.UTF_8);

            String url = DB_URL + "/rest/v1/user_goal_history?goal_date=eq." + encodedDate;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", DB_KEY)
                    // Add Authorization token to the request
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .header("Prefer", "return=representation")
                    .DELETE()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new IOException("Failed to delete entry with date " + goalDate + ": " + response.body());
            }
        }
    }
}

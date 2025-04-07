package org.aimacrosapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GoalsLogic {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");

    private HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
        URL url = new URL(DB_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
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

    public boolean upsertGoals(String goal_date, String workouts_per_week, String daily_calories, String daily_carbs,
                               String daily_protein, String daily_fats, String user_id) throws IOException {
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        String jsonInputString = String.format(
                "{\"goal_date\":\"%s\", \"workouts_per_week\":\"%s\", \"daily_calories\":\"%s\", \"daily_carbs\":\"%s\", " +
                        "\"daily_protein\":\"%s\", \"daily_fats\":\"%s\", \"user_id\":\"%s\"}",
                goal_date, workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats, user_id
        );

        boolean exists = checkIfUserGoalsExist(user_id);
        String endpoint = exists ? "/rest/v1/user_goals?user_id=eq." + user_id : "/rest/v1/user_goals";
        HttpURLConnection conn = setupConnection(endpoint, exists ? "PATCH" : "POST");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        System.out.println((exists ? "Updating" : "Creating") + " user_goals entry with JSON: " + jsonInputString);

        try (Scanner scanner = new Scanner(
                responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED
                        ? conn.getInputStream()
                        : conn.getErrorStream()
        )) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            System.out.println("Response: " + response.toString());
        }

        return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
    }

    public boolean createGoalHistory(String todays_workout, String todays_calories, String todays_carbs,
                                     String todays_protein, String todays_fats, String user_id) throws IOException {
        if (user_id == null || user_id.isEmpty()) {
            System.out.println("Invalid user_id provided");
            return false;
        }

        String jsonInputString = String.format(
                "{\"todays_workout\":\"%s\", \"todays_calories\":\"%s\", \"todays_carbs\":\"%s\", " +
                        "\"todays_protein\":\"%s\", \"todays_fats\":\"%s\", \"user_id\":\"%s\"}",
                todays_workout, todays_calories, todays_carbs, todays_protein, todays_fats, user_id
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


//package org.aimacrosapp;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Scanner;
//
//public class GoalsLogic {
//    //dotenv allows me to save keys in env file
//    private static final Dotenv dotenv = Dotenv.load();
//    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
//    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");
//
//    //method to establish HTTP connections (goals insertion only)
//    public HttpURLConnection establishDatabaseConnection(String goal_date, String workouts_per_week, String daily_calories, String daily_carbs, String daily_protein, String daily_fats) throws IOException {
//        //endpoint is the url for the user table
//        String endpoint = DB_URL + "/rest/v1/user_goals";
//        URL url = new URL(endpoint);
//        //open HTTP connection for API call
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        //connecting to supabase via API request to post data to user goal table
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Prefer", "return=representation");
//        conn.setRequestProperty("apikey", DB_KEY);
//        conn.setDoOutput(true);
//        return conn;
//    }
//
//    //overloaded method to establish HTTP connections (goal history insertion only)
//    public HttpURLConnection establishDatabaseConnection(String todays_workout, String todays_calories, String todays_carbs, String todays_protein, String todays_fats) throws IOException {
//        //endpoint is the url for the user goa history table
//        String endpoint = DB_URL + "/rest/v1/user_goal_history";
//        URL url = new URL(endpoint);
//        //open HTTP connection for API call
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        //connecting to supabase via API request to post data to user goal history table
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Prefer", "return=representation");
//        conn.setRequestProperty("apikey", DB_KEY);
//        conn.setDoOutput(true);
//        return conn;
//    }
//
//    // Method to get the user id from the JSON response
//    private String extractUserId(String jsonResponse) {
//        // Ensure response is not empty
//        if (jsonResponse == null || jsonResponse.isEmpty()) {
//            return null;
//        }
//        try {
//            // If response is an array, parse it as JSONArray
//            JSONArray jsonArray = new JSONArray(jsonResponse);
//            if (jsonArray.length() > 0) {
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                return jsonObject.getString("user_id"); // Extract "user_id"
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null; // Return null if extraction fails
//    }
//
//    // Method to create a new goal set (bypassing authentication)
//    public boolean createGoals(String goal_date, String workouts_per_week, String daily_calories, String daily_carbs, String daily_protein, String daily_fats, String user_id) throws IOException {
//        // Prepare JSON input for insertion into user table
//        String jsonInputString = String.format(
//                "{\"goal_date\":\"%s\", \"workouts_per_week\":\"%s\", \"daily_calories\":\"%s\", \"daily_carbs\":\"%s\", \"daily_protein\":\"%s\", \"daily_fats\":\"%s\"," +
//                        "\"user_id\":\"%s\"}",
//                goal_date, workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats, user_id
//        );
//
//        //call to HTTP connection
//        HttpURLConnection conn = establishDatabaseConnection(goal_date, workouts_per_week, daily_calories, daily_carbs, daily_protein, daily_fats);
//
//        //json response
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//
//            // Get the response code from the API
//            int responseCode = conn.getResponseCode();
//
//            System.out.println("Sending JSON: " + jsonInputString);
//
//            // Handle the response based on the status code
//            if (responseCode == HttpURLConnection.HTTP_CREATED) {
//                // Success, print the response body
//                try (Scanner scanner = new Scanner(conn.getInputStream())) {
//                    StringBuilder jsonResponse = new StringBuilder();
//                    while (scanner.hasNext()) {
//                        jsonResponse.append(scanner.nextLine());
//                    }
//                    System.out.println("Raw JSON response: " + jsonResponse.toString());
//                }
//            } else {
//                // Error handling
//                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
//                    while (scanner.hasNext()) {
//                        System.out.println(scanner.nextLine());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    // Method to create a new goal progress and insert into the database
//    public boolean createGoalHistory(String todays_workout, String todays_calories, String todays_carbs, String todays_protein, String todays_fats, String user_id) throws IOException {
//        //if no user id provided to user account, show error msg
//        if (user_id == null || user_id.isEmpty()) {
//            System.out.println("Invalid user_id provided");
//            return false;
//        }
//
//        // Prepare JSON input for insertion into user account table
//        String jsonInputString = String.format(
//                "{\"todays_workout\":\"%s\", \"todays_calories\":\"%s\", \"todays_carbs\":\"%s\", \"todays_protein\":\"%s\", \"todays_fats\":\"%s\", \"user_id\":\"%s\", }",
//                todays_workout, todays_calories, todays_carbs, todays_protein, todays_fats, user_id
//        );
//
//        //call to HTTP connection
//        HttpURLConnection conn = establishDatabaseConnection(todays_workout, todays_calories, todays_carbs, todays_protein, todays_fats);
//
//        //JSON code
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//
//            // Get the response code from the API
//            int responseCode = conn.getResponseCode();
//            //System.out.println("Response Code: " + responseCode);
//
//            System.out.println("Sending JSON: " + jsonInputString);
//            // Handle the response based on the status code
//            if (responseCode == HttpURLConnection.HTTP_CREATED) {
//                // Success
//                try (Scanner scanner = new Scanner(conn.getInputStream())) {
//                    while (scanner.hasNext()) {
//                        System.out.println(scanner.nextLine());
//                    }
//                }
//                return true;
//            } else {
//                // Error handling
//                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
//                    StringBuilder errorResponse = new StringBuilder();
//                    while (scanner.hasNext()) {
//                        errorResponse.append(scanner.nextLine());
//                    }
//                    System.out.println("Error Response: " + errorResponse.toString());
//                }
//                return false;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false; // Catch any exceptions and return false
//        }
//    }
//
//}

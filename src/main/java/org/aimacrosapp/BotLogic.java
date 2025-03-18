package org.aimacrosapp;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class BotLogic {
    //dotenv allows me to save keys in env file
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BOT_URL = dotenv.get("MACROS_APP_BOTPRESS_URL");
    private static final String BOT_KEY = dotenv.get("MACROS_APP_BOTPRESS_API_KEY");

    public HttpResponse<String> connectToBot(String userMessage) throws IOException, InterruptedException {
        // JSON payload for the message
        String requestBody = String.format("{\"type\": \"text\", \"text\": \"%s\"}", userMessage);

        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BOT_URL))
                .header("Authorization", "Bearer " + BOT_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print chatbot response
        System.out.println("Chatbot Response: " + response.body());
        //log URL and API Key
        System.out.println("Bot URL: " + BOT_URL);
        System.out.println("Bot Key: " + BOT_KEY);

        // Return response to process elsewhere
        return response;
    }
}

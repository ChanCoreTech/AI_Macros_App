package org.aimacrosapp;

import io.github.cdimascio.dotenv.Dotenv;

import java.awt.*;
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
    private static final String BOT_LINK_URL = dotenv.get("MACROS_APP_BOTPRESS_URL_LINK");

    public void linkToBot() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(BOT_LINK_URL));
                System.out.println("Opening bot session: " + BOT_LINK_URL);
            } else {
                System.out.println("Desktop browsing is not supported on this system.");
            }
        } catch (IOException e) {
            System.err.println("Error opening bot session: " + e.getMessage());
        }
    }
}

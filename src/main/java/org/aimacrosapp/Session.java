package org.aimacrosapp;

public class Session {
    private static String email;

    public static void setEmail(String userEmail) {
        email = userEmail;
    }

    public static String getEmail() {
        return email;
    }

    public static void clear() {
        email = null;
    }
}
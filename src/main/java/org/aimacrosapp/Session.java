package org.aimacrosapp;

import java.util.prefs.Preferences;

public class Session {
    private static String email;
    private static String accessToken;
    private static String tempUserId;

    private static User currentUser;
    private static UserAccount currentUserAccount;
    private static UserGoals currentUserGoals;
    private static UserGoalHistory currentUserGoalHistory;

    private static final Preferences prefs = Preferences.userRoot().node("AI-Macros-Session");

    //gets and sets

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUserAccount(UserAccount userAccount) {
        currentUserAccount = userAccount;
    }

    public static UserAccount getCurrentUserAccount() {
        return currentUserAccount;
    }

    public static UserGoals getCurrentUserGoals() {
        return currentUserGoals;
    }

    public static void setCurrentUserGoals(UserGoals currentUserGoals) {
        Session.currentUserGoals = currentUserGoals;
    }

    public static UserGoalHistory getCurrentUserGoalHistory() {
        return currentUserGoalHistory;
    }

    public static void setCurrentUserGoalHistory(UserGoalHistory currentUserGoalHistory) {
        Session.currentUserGoalHistory = currentUserGoalHistory;
    }

    public static void setEmail(String e) {
        email = e;
        prefs.put("email", e); // persist email
    }

    public static String getEmail() {
        return email;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setTempUserId(String userId) {
        tempUserId = userId;
    }

    public static String getTempUserId() {
        return tempUserId;
    }

    //clear user's current session
    public static void clear() {
        email = null;
        currentUser = null;
        currentUserAccount = null;
        currentUserGoals = null;
        currentUserGoalHistory = null;
        tempUserId = null;
        prefs.remove("email");
    }
}
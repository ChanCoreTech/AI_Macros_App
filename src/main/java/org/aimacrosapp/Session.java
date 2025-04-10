package org.aimacrosapp;

public class Session {
    private static String email;

    private static User currentUser;
    private static UserAccount currentUserAccount;

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
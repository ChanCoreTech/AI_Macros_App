package org.aimacrosapp;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountLogic {
    //don't hardcode
    //https://www.youtube.com/watch?v=Gz9bvYybaws&list=PL4cUxeGkcC9hUb6sHthUEwG7r9VDPBMKO
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("MACROS_APP_SUPABASE_URL");
    private static final String DB_KEY = dotenv.get("MACROS_APP_ANON_KEY");
    private static final String DB_PASSWORD = dotenv.get("MACROS_APP_PASSWORD");
    private static final String DB_USERNAME = dotenv.get("MACROS_APP_USERNAME");

    //method to create a user
    public User newUser(){
     System.out.println("Please type 'yes' in order to create an account");
     Scanner s = new Scanner(System.in);
     String input = s.nextLine();

        //User user1 = new User("John", "Doe", "1990-04-25", "Male", 5, 10);

        if(input.equals("yes")) {
            User user1 = new User("Sally", "Doe", "1990-04-25", "Female", 5, 10);
            UserAccount userAccount1 = new UserAccount(user1, "john.doe@example.com", "password123", "johndoe", "123-456-7890");
            System.out.println("Account created! Thanks a lot, " + user1.getFirst_name() + "!");
            return user1;
        }
        return null;
    }

    //method to send user to supabase
    public void sendUser(User user) throws ClassNotFoundException {
        //Class.forName("org.postgresql.Driver");
        System.out.println(DB_URL);
        String query = "INSERT INTO user (first_name, last_name, birth_date, gender, height_feet, height_inches) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println("DB_URL: " + DB_URL);
        System.out.println("DB_USERNAME: " + DB_USERNAME);
        System.out.println("DB_PASSWORD: " + DB_PASSWORD);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getFirst_name());
            pstmt.setString(2, user.getLast_name());
            pstmt.setString(3, user.getBirth_date());
            pstmt.setString(4, user.getGender());
            pstmt.setInt(5, user.getHeight_feet());
            pstmt.setInt(6, user.getHeight_inches());

            pstmt.executeUpdate();
            System.out.println("User data successfully added to Supabase!");

        } catch (SQLException e) {
            System.out.println("Database connection error:");
            e.printStackTrace();
        }
    }
}

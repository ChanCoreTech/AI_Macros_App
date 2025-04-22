package org.aimacrosapp;
import javax.swing.*;
import java.io.IOException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {

        //• ADD SESSIONS
        //• TOUCH UP CHATBOT
        //• ADD LOGO TO EVERY PAGE TASKBAR
        // • ADD FORGOT PASSWORD VALIDATION AND EXTRA QUESTIONS
        //• ADD HOVER TEXT AND TOUCH UP GUI

        //• DO WORD AND POWERPOINT

        // • DON'T EXPOSE ENV
        // • CHANGE COMMENTS
        // • RE-WORD LABELS
        // • IMAGE RESOLUTION

        // • TEST .JAR

        //try {
            //object for AccountLogic class
            //AccountLogic accountLogic = new AccountLogic();

            //object for SignInGUI class
            //SignInGUI gui = new SignInGUI();

            //start up SignInGUI
            SwingUtilities.invokeLater(() -> new SignInGUI());

            //SEND TO BOT
            //botLogic.linkToBot();

            //DELETE FIELD FROM USERS
            //AccountLogic.deleteUser(UUID.fromString("1102ca65-6133-44b9-86bc-bb6e7602c7db"));

            //UPDATE FIELD FROM USERS
           //accountLogic.updateUserField(UUID.fromString("929f6354-11b2-4381-bc1c-9d2ba7b0031f"));

            //UPDATE FIELD FROM USER ACCOUNT
            //accountLogic.updateUserAccountField(UUID.fromString("a09d1ef5-0027-4d86-9c45-7d64763c2832"));

            //CREATE USER
            //String userId = accountLogic.createUser("Isaac", "Doe", "1990-01-01", "Male", 5, 9, "medium", null, null, "lose weight");

//            if(userId != null) {
//                //CREATE USER ACCOUNT
//                boolean userAccountCreated = accountLogic.createUserAccount("jdoe1@example.com", "jdoe123", "Cool Dood", "803-502-6589", null, userId);
//            if(userAccountCreated) {
//                System.out.println("User account created successfully!");
//            }
//            else{
//                System.out.println("Failed to create user account!");
//            }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

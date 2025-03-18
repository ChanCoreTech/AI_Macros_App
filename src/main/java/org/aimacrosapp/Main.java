package org.aimacrosapp;
import java.io.IOException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        //try {
            // Example: Hard-coded user data
            //object for AccountLogic class
            AccountLogic accountLogic = new AccountLogic();

            //object for BotLogic class
            BotLogic botLogic = new BotLogic();

            //SEND MESSAGE TO BOT
            botLogic.connectToBot("Hello!");

            //DELETE FIELD FROM USERS
            //AccountLogic.deleteUser(UUID.fromString("1102ca65-6133-44b9-86bc-bb6e7602c7db"));

            //UPDATE FIELD FROM USERS
           //accountLogic.updateUserField(UUID.fromString("1102ca65-6133-44b9-86bc-bb6e7602c7db"));

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
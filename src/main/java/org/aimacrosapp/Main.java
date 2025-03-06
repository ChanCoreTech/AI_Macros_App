package org.aimacrosapp;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        //test

    AccountLogic a = new AccountLogic();
        AccountLogic deletethis = new AccountLogic();
    User u = a.newUser();
    a.sendUser(u);

        //System.out.println("Available JDBC Drivers:");

//        // Convert Enumeration to List
//        List<Driver> drivers = Collections.list(DriverManager.getDrivers());
//
//        // Now foreach loop works
//        for (Driver driver : drivers) {
//            System.out.println(driver.getClass().getName());
//        }
    }
}
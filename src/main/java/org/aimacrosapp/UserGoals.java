package org.aimacrosapp;
import org.aimacrosapp.User;

public class UserGoals {
    //attributes
    private User user;
    private String workouts_per_week;
    private int daily_calories;
    private int daily_carbs;
    private int daily_protein;
    private int daily_fats;

    UserGoals(){

    }

    //necessary user goals constructor
    public UserGoals(User user, String workouts_per_week, int daily_calories, int daily_carbs, int daily_protein, int daily_fats){
        this.user = user;
        this.workouts_per_week = workouts_per_week;
        this.daily_calories = daily_calories;
        this.daily_carbs = daily_carbs;
        this.daily_protein = daily_protein;
        this.daily_fats = daily_fats;
    }

    //getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWorkouts_per_week() {
        return workouts_per_week;
    }

    public void setWorkouts_per_week(String workouts_per_week) {
        this.workouts_per_week = workouts_per_week;
    }

    public int getDaily_calories() {
        return daily_calories;
    }

    public void setDaily_calories(int daily_calories) {
        this.daily_calories = daily_calories;
    }

    public int getDaily_carbs() {
        return daily_carbs;
    }

    public void setDaily_carbs(int daily_carbs) {
        this.daily_carbs = daily_carbs;
    }

    public int getDaily_protein() {
        return daily_protein;
    }

    public void setDaily_protein(int daily_protein) {
        this.daily_protein = daily_protein;
    }

    public int getDaily_fats() {
        return daily_fats;
    }

    public void setDaily_fats(int daily_fats) {
        this.daily_fats = daily_fats;
    }
}

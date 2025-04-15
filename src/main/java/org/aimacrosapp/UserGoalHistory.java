package org.aimacrosapp;

public class UserGoalHistory {
    //attributes
    private User user;
    private String goal_date;
    private boolean todays_workout;
    private int todays_calories;
    private int todays_carbs;
    private int todays_protein;
    private int todays_fats;

    public UserGoalHistory(){

    }

    //necessary user goals constructor
    public UserGoalHistory(User user, String goal_date, boolean todays_workout, int todays_calories, int todays_carbs, int todays_protein, int todays_fats){
        this.user = user;
        this.goal_date = goal_date;
        this.todays_workout = todays_workout;
        this.todays_calories = todays_calories;
        this.todays_carbs = todays_carbs;
        this.todays_protein = todays_protein;
        this.todays_fats = todays_fats;
    }

    //getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGoal_date() {
        return goal_date;
    }

    public void setGoal_date(String goal_date) {
        this.goal_date = goal_date;
    }

    public boolean isTodays_workout() {
        return todays_workout;
    }

    public void setTodays_workout(boolean todays_workout) {
        this.todays_workout = todays_workout;
    }

    public int getTodays_calories() {
        return todays_calories;
    }

    public void setTodays_calories(int todays_calories) {
        this.todays_calories = todays_calories;
    }

    public int getTodays_carbs() {
        return todays_carbs;
    }

    public void setTodays_carbs(int todays_carbs) {
        this.todays_carbs = todays_carbs;
    }

    public int getTodays_protein() {
        return todays_protein;
    }

    public void setTodays_protein(int todays_protein) {
        this.todays_protein = todays_protein;
    }

    public int getTodays_fats() {
        return todays_fats;
    }

    public void setTodays_fats(int todays_fats) {
        this.todays_fats = todays_fats;
    }
}

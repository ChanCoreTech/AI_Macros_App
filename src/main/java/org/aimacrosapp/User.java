package org.aimacrosapp;
//class for user account information
public class User {
    //attributes
    private String first_name;
    private String last_name;
    private String birth_date;
    private String gender;
    private int height_feet;
    private int height_inches;

    //temp
    private String email;
    private String password;
    //temp

    //optional attributes
    private String body_type;
    private String experience_level;
    private String activity_level;
    private String primary_goal;
    //feel free to add more if necessary

    //necessary user constructor
    public User(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches){
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.height_feet = height_feet;
        this.height_inches = height_inches;
    }

    //TEMP necessary user constructor
    public User(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches, String email, String password){
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.height_feet = height_feet;
        this.height_inches = height_inches;
        this.email = email;
        this.password = password;
    }

    //optional user constructor
    public User(String first_name, String last_name, String birth_date, String gender, int height_feet, int height_inches, String body_type, String experience_level,
                String activity_level, String primary_goal){
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.height_feet = height_feet;
        this.height_inches = height_inches;
        this.body_type = body_type;
        this.experience_level = experience_level;
        this.activity_level = activity_level;
        this.primary_goal = primary_goal;
    }

    //setters and getters
    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight_feet() {
        return height_feet;
    }

    public void setHeight_feet(int height_feet) {
        this.height_feet = height_feet;
    }

    public int getHeight_inches() {
        return height_inches;
    }

    public void setHeight_inches(int height_inches) {
        this.height_inches = height_inches;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getExperience_level() {
        return experience_level;
    }

    public void setExperience_level(String experience_level) {
        this.experience_level = experience_level;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }

    public String getPrimary_goal() {
        return primary_goal;
    }

    public void setPrimary_goal(String primary_goal) {
        this.primary_goal = primary_goal;
    }
}

package org.aimacrosapp;
public class UserAccount {
    //attributes
    private User user;
    private String email;
    private String password;
    private String nickname;
    private String phone_number;
    //optional attributes
    private String email_second;

    //empty
    public UserAccount() {

    }

    //necessary user account constructor
    public UserAccount(User user, String email, String password, String nickname, String phone_number){
        this.user = user;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone_number = phone_number;
    }
    //optional user account constructor
    public UserAccount(User user, String email, String password, String nickname, String phone_number, String email_second){
        this.user = user;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone_number = phone_number;
        this.email_second = email_second;
    }

    //setters and getters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_second() {
        return email_second;
    }

    public void setEmail_second(String email_second) {
        this.email_second = email_second;
    }
}

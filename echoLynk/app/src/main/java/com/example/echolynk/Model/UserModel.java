package com.example.echolynk.Model;

import com.google.firebase.Timestamp;

public class UserModel {

    private String userName;
    private String phoneNumber;
    private String email;
    private Timestamp createdTimestamp;
    private String userId;
    private String password;
    private String role;
    private boolean isPremium;

    public UserModel() {
    }

    public UserModel(String userName, String phoneNumber, String email, Timestamp createdTimestamp, String userId, String password, String role, boolean isPremium) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.isPremium = isPremium;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getTimestamp() {
        return createdTimestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.createdTimestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsPremium() {
        return isPremium;
    }
    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
}

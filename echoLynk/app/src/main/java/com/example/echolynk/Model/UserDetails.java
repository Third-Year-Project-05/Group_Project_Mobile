package com.example.echolynk.Model;

public class UserDetails {
    private String userId;
    private String age;
    private String communicationMode;
    private String educationalLevel;
    private String gender;
    private String schoolName;
    private String role;
    private String reason;

    public UserDetails(String userId, String age, String communicationMode, String educationalLevel, String gender, String schoolName, String role, String reason) {
        this.userId = userId;
        this.age = age;
        this.communicationMode = communicationMode;
        this.educationalLevel = educationalLevel;
        this.gender = gender;
        this.schoolName = schoolName;
        this.role = role;
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(String communicationMode) {
        this.communicationMode = communicationMode;
    }

    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

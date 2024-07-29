package com.example.echolynk.Model;

public class HistoryMassage {

    private String role;
    private String content;

    public HistoryMassage() {
    }

    public HistoryMassage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

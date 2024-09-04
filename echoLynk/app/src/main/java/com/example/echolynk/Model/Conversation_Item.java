package com.example.echolynk.Model;

public class Conversation_Item {
    private int conversationId;
    private String title;
    private String description;
    private String conversationLastMassage;
    private String date;
    private String time;
    private String endTime;

    public Conversation_Item(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public Conversation_Item(int conversationId, String title, String conversationLastMassage, String date, String time, String endTime) {
        this.conversationId = conversationId;
        this.title = title;
        this.conversationLastMassage = conversationLastMassage;
        this.date = date;
        this.time = time;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationLastMassage() {
        return conversationLastMassage;
    }

    public void setConversationLastMassage(String conversationLastMassage) {
        this.conversationLastMassage = conversationLastMassage;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

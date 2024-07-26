package com.example.echolynk.Model;

public class Call_Item {
    String name;
    String lastMessage;
    String time; //date
    String count;
    int image;

    public Call_Item(String name, String lastMessage, String time, String count, int image) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.count = count;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
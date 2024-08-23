package com.example.echolynk.Model;

import java.time.LocalDate;
import java.util.List;

public class LiveChatRoomModel {
    private LocalDate currentDate;
    private String conversationStartTime;
    private String conversationEndTime;
    private List<MassageModel> massages;
    private String lastMassage;

    public LiveChatRoomModel(LocalDate currentDate, String conversationStartTime, String conversationEndTime, List<MassageModel> massages, String lastMassage) {
        this.setCurrentDate(currentDate);
        this.setConversationStartTime(conversationStartTime);
        this.setConversationEndTime(conversationEndTime);
        this.setMassages(massages);
        this.setLastMassage(lastMassage);
    }

    public LiveChatRoomModel() {
    }


    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public String getConversationStartTime() {
        return conversationStartTime;
    }

    public void setConversationStartTime(String conversationStartTime) {
        this.conversationStartTime = conversationStartTime;
    }

    public String getConversationEndTime() {
        return conversationEndTime;
    }

    public void setConversationEndTime(String conversationEndTime) {
        this.conversationEndTime = conversationEndTime;
    }

    public List<MassageModel> getMassages() {
        return massages;
    }

    public void setMassages(List<MassageModel> massages) {
        this.massages = massages;
    }

    public String getLastMassage() {
        return lastMassage;
    }

    public void setLastMassage(String lastMassage) {
        this.lastMassage = lastMassage;
    }
}

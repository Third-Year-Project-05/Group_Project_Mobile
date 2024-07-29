package com.example.echolynk.Model;

import java.util.ArrayList;
import java.util.List;

public class SpeechToTextMassage {
    private String massage;
    private ArrayList<HistoryMassage> history;
    private ArrayList<PersonalData> personalData;


    public SpeechToTextMassage(String massage, ArrayList<HistoryMassage> history, ArrayList<PersonalData> personalData) {
        this.massage = massage;
        this.history = history;
        this.personalData = personalData;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public ArrayList<HistoryMassage> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<HistoryMassage> history) {
        this.history = history;
    }

    public ArrayList getPersonalData() {
        return personalData;
    }

    public void setPersonalData(ArrayList personalData) {
        this.personalData = personalData;
    }
}

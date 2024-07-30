package com.example.echolynk.Model;

public class MassageModel {
    private String massage;
    private int type;

    public MassageModel(String massage, int type) {
        this.massage = massage;
        this.type = type;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

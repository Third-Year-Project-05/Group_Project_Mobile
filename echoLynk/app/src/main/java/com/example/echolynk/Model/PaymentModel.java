package com.example.echolynk.Model;

import com.google.firebase.Timestamp;

public class PaymentModel {
    private String id;
    private int imageCount;
    private int suggestionCount;
    private double totalCost;

    public PaymentModel(String id, int imageCount, int suggestionCount, double totalCost, Timestamp paymentDate) {
        this.id = id;
        this.imageCount = imageCount;
        this.suggestionCount = suggestionCount;
        this.totalCost = totalCost;
        this.paymentDate = paymentDate;
    }

    private Timestamp paymentDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getSuggestionCount() {
        return suggestionCount;
    }

    public void setSuggestionCount(int suggestionCount) {
        this.suggestionCount = suggestionCount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}

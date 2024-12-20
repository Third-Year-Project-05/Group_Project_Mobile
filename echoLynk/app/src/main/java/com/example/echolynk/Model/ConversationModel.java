package com.example.echolynk.Model;

import java.util.List;

public class ConversationModel {

    private String question;
    private List<String>  answer;

    public ConversationModel() {
    }

    public ConversationModel(String question, List<String> answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}

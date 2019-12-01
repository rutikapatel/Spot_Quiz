package com.example.spotquiz.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    private String question;
    private ArrayList<String> options;

    public Question(String question, ArrayList<String> options, int correctAnswer) { 
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    private int correctAnswer;
}

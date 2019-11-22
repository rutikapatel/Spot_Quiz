package com.example.spotquiz.pojo;

public class Quiz {
String quizName,courseName,noOfQuestions,quizKey;
QuizLocation quizLocation;

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(String noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getQuizKey() {
        return quizKey;
    }

    public void setQuizKey(String quizKey) {
        this.quizKey = quizKey;
    }

    public QuizLocation getQuizLocation() {
        return quizLocation;
    }

    public void setQuizLocation(QuizLocation quizLocation) {
        this.quizLocation = quizLocation;
    }
}

package com.example.spotquiz.pojo;

public class Quiz {
String quizName,courseName,noOfQuestions,quizKey,quizDate,quizStartTime,quizLength;

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public String getQuizStartTime() {
        return quizStartTime;
    }

    public void setQuizStartTime(String quizStartTime) {
        this.quizStartTime = quizStartTime;
    }

    public String getQuizLength() {
        return quizLength;
    }

    public void setQuizLength(String quizLength) {
        this.quizLength = quizLength;
    }

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

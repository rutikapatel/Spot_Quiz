package com.example.spotquiz.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {
    String quizName,courseName,noOfQuestions,quizKey,quizDate,quizStartTime,quizLength,professorId;
    Boolean active;
    ArrayList<Question> questions;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Quiz(String quizName, String courseName, String noOfQuestions, String quizKey, String quizDate, String quizStartTime, String quizLength, QuizLocation quizLocation,Boolean active, ArrayList<Question> questions,String professorId) {
        this.quizName = quizName;
        this.courseName = courseName;
        this.noOfQuestions = noOfQuestions;
        this.quizKey = quizKey;
        this.quizDate = quizDate;
        this.quizStartTime = quizStartTime;
        this.quizLength = quizLength;
        this.quizLocation = quizLocation;
        this.active = active;
        this.questions = questions;
        this.professorId = professorId;

    }

    public Quiz() {
    }

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

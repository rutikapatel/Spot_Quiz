package com.example.spotquiz.pojo;

public class QuizResult {
    String quizId,img,quizName,Result;

    public QuizResult(String quizId, String img, String quizName, String result) {
        this.quizId = quizId;
        this.img = img;
        this.quizName = quizName;
        Result = result;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public QuizResult() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}

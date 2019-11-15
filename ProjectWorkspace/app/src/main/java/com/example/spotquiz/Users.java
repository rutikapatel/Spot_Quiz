package com.example.spotquiz;

public class Users {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getProfessor() {
        return professor;
    }

    public void setProfessor(Boolean professor) {
        this.professor = professor;
    }

    public Boolean getStudent() {
        return student;
    }

    public void setStudent(Boolean student) {
        this.student = student;
    }

    String email;
    String name;

    public String getDalId() {
        return dalId;
    }

    public void setDalId(String dalId) {
        this.dalId = dalId;
    }

    String dalId;
    Boolean professor,student;
}

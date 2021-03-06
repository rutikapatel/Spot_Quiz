package com.example.spotquiz.pojo;

public class Users {
    String email;
    String name;
    String dalId;
    Boolean professor, student;
    String profilePhoto;
    String userId;

    public Users(String email, String name, String dalId, Boolean professor, Boolean student, String profilePhoto, String userId) {
        this.email = email;
        this.name = name;
        this.dalId = dalId;
        this.professor = professor;
        this.student = student;
        this.profilePhoto = profilePhoto;
        this.userId = userId;
    }

    public Users() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


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


    public String getDalId() {
        return dalId;
    }

    public void setDalId(String dalId) {
        this.dalId = dalId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }


}

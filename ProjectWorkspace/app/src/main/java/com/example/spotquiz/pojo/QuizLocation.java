package com.example.spotquiz.pojo;

import java.io.Serializable;

public class QuizLocation implements Serializable {
    String name;
    Double longitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    Double latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

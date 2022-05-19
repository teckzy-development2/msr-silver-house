package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class ChildReviewsPojo {
    @SerializedName("image")
    String image;
    @SerializedName("name")
    String name;
    @SerializedName("rate")
    String rate;
    @SerializedName("message")
    String message;
    @SerializedName("date")
    String date;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

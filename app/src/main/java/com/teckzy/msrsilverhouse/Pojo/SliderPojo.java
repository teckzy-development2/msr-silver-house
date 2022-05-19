package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class SliderPojo {
    @SerializedName("category_id")
    String category_id;
    @SerializedName("category_name")
    String category_name;
    @SerializedName("category_image")
    String category_image;
    //Product details slider
    @SerializedName("image")
    String image;
    @SerializedName("banner_id")
    String banner_id;
    @SerializedName("banner_name")
    String banner_name;
    @SerializedName("banner_image")
    String banner_image;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
}

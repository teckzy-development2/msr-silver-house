package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class BasketPojo {
    @SerializedName("basket_id")
    String basket_id;
    @SerializedName("basket_weight")
    String basket_weight;
    @SerializedName("basket_image")
    String basket_image;
    Boolean selected = false;

    public String getBasket_id() {
        return basket_id;
    }

    public void setBasket_id(String basket_id) {
        this.basket_id = basket_id;
    }

    public String getBasket_weight() {
        return basket_weight;
    }

    public void setBasket_weight(String basket_weight) {
        this.basket_weight = basket_weight;
    }

    public String getBasket_image() {
        return basket_image;
    }

    public void setBasket_image(String basket_image) {
        this.basket_image = basket_image;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscribtionDetailsPojo {
    @SerializedName("subscribtion_id")
    int subscribtion_id;
    @SerializedName("subscribtion_name")
    String subscribtion_name;
    @SerializedName("image")
    String image;
    @SerializedName("products")
    String products;
    @SerializedName("weight")
    String weight;
    @SerializedName("amount")
    String amount;
    @SerializedName("weight_amount")
    List<ChildPricePojo> weight_amount;

    public List<ChildPricePojo> getWeight_amount() {
        return weight_amount;
    }

    public void setWeight_amount(List<ChildPricePojo> weight_amount) {
        this.weight_amount = weight_amount;
    }

    public int getSubscribtion_id() {
        return subscribtion_id;
    }

    public void setSubscribtion_id(int subscribtion_id) {
        this.subscribtion_id = subscribtion_id;
    }

    public String getSubscribtion_name() {
        return subscribtion_name;
    }

    public void setSubscribtion_name(String subscribtion_name) {
        this.subscribtion_name = subscribtion_name;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class SuggestionPojo {
    @SerializedName("product_name")
    String product_name;
    @SerializedName("product_id")
    int product_id;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}

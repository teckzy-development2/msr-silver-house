package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrdersPojo {
    @SerializedName("order_id")
    String order_id;
    @SerializedName("order_unique_id")
    String order_unique_id;
    @SerializedName("order_date")
    String order_date;
    @SerializedName("estimation_delivery")
    String estimation_delivery;
    @SerializedName("products")
    List<MyordersSubPojo> products;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_unique_id() {
        return order_unique_id;
    }

    public void setOrder_unique_id(String order_unique_id) {
        this.order_unique_id = order_unique_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getEstimation_delivery() {
        return estimation_delivery;
    }

    public void setEstimation_delivery(String estimation_delivery) {
        this.estimation_delivery = estimation_delivery;
    }

    public List<MyordersSubPojo> getProducts() {
        return products;
    }

    public void setProducts(List<MyordersSubPojo> products) {
        this.products = products;
    }
}

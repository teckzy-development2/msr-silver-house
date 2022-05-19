package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscribtionCartPojo {
    @SerializedName("items")
    List<SubscribtionDetailsPojo> items;
    @SerializedName("total")
    String total;

    public List<SubscribtionDetailsPojo> getItems() {
        return items;
    }

    public void setItems(List<SubscribtionDetailsPojo> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

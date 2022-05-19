package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartPojo
{
    @SerializedName("items")
    List<ChildCartPojo> items;
    @SerializedName("total")
    String total;

    public List<ChildCartPojo> getItems() {
        return items;
    }

    public void setItems(List<ChildCartPojo> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CheckoutPojo {
    @SerializedName("address")
    List<AddressPojo> address;
    @SerializedName("items")
    List<ChildCartPojo> items;
    @SerializedName("subscribtion")
    List<SubscribtionDetailsPojo> subscribtion;
    @SerializedName("cart_value")
    String cart_value;
    @SerializedName("discount")
    String discount;
    @SerializedName("taxes")
    String taxes;
    @SerializedName("delivery_charges")
    String delivery_charges;
    @SerializedName("total_pay")
    String total_pay;

    public List<AddressPojo> getAddress() {
        return address;
    }

    public void setAddress(List<AddressPojo> address) {
        this.address = address;
    }

    public List<ChildCartPojo> getItems() {
        return items;
    }

    public void setItems(List<ChildCartPojo> items) {
        this.items = items;
    }

    public List<SubscribtionDetailsPojo> getSubscribtion() {
        return subscribtion;
    }

    public void setSubscribtion(List<SubscribtionDetailsPojo> subscribtion) {
        this.subscribtion = subscribtion;
    }

    public String getCart_value() {
        return cart_value;
    }

    public void setCart_value(String cart_value) {
        this.cart_value = cart_value;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay = total_pay;
    }
}

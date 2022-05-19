package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class ChildCartPojo
{
    @SerializedName("cart_id")
    int cart_id;
    @SerializedName("product_id")
    int product_id;
    @SerializedName("product_name")
    String product_name;
    @SerializedName("image")
    String image;
    @SerializedName("price")
    String price;
    @SerializedName("qty")
    int qty;

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
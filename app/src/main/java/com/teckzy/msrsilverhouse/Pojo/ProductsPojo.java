package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsPojo
{
    @SerializedName("product_id")
    int product_id;
    @SerializedName("product_name")
    String product_name;
    @SerializedName("image")
    String image;
    @SerializedName("price")
    String price;
    @SerializedName("description")
    String description;
    @SerializedName("slider")
    List<SliderPojo> slider;
    @SerializedName("wishlist")
    String wishlist;
    @SerializedName("reviews")
    List<ChildReviewsPojo> reviews;
    Boolean selected = false;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SliderPojo> getSlider() {
        return slider;
    }

    public void setSlider(List<SliderPojo> slider) {
        this.slider = slider;
    }

    public List<ChildReviewsPojo> getReviews() {
        return reviews;
    }

    public void setReviews(List<ChildReviewsPojo> reviews) {
        this.reviews = reviews;
    }

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
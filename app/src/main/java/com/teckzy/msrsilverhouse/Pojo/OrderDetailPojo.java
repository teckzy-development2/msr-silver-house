package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailPojo {
    @SerializedName("order_id")
    String order_id;
    @SerializedName("unique_id")
    String unique_id;
    @SerializedName("date_time")
    String date_time;
    @SerializedName("products")
    List<MyordersSubPojo> products;
    @SerializedName("total_payable")
    String total_payable;
    @SerializedName("estimated_delivery")
    String estimated_delivery;
    @SerializedName("order_status")
    String order_status;
    @SerializedName("name")
    String name;
    @SerializedName("door_no")
    String door_no;
    @SerializedName("street")
    String street;
    @SerializedName("landmark")
    String landmark;
    @SerializedName("city")
    String city;
    @SerializedName("state")
    String state;
    @SerializedName("pincode")
    String pincode;
    @SerializedName("district")
    String district;
    @SerializedName("country")
    String country;
    @SerializedName("mobile")
    String mobile;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getTotal_payable() {
        return total_payable;
    }

    public void setTotal_payable(String total_payable) {
        this.total_payable = total_payable;
    }

    public String getEstimated_delivery() {
        return estimated_delivery;
    }

    public void setEstimated_delivery(String estimated_delivery) {
        this.estimated_delivery = estimated_delivery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoor_no() {
        return door_no;
    }

    public void setDoor_no(String door_no) {
        this.door_no = door_no;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<MyordersSubPojo> getProducts() {
        return products;
    }

    public void setProducts(List<MyordersSubPojo> products) {
        this.products = products;
    }
}

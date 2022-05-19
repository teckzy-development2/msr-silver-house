package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class AddressPojo {
    @SerializedName("address_id")
    int address_id;
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
    @SerializedName("mobile")
    String mobile;
    @SerializedName("address_type")
    String address_type;
    @SerializedName("district")
    String district;
    @SerializedName("country")
    String country;

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }
}
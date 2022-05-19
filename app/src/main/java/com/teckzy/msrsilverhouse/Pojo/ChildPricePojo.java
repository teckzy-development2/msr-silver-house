package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class ChildPricePojo {
    @SerializedName("kg_price")
    int kg_price;
    @SerializedName("dozen_price")
    int dozen_price;
    @SerializedName("piece_price")
    int piece_price;
    @SerializedName("box_price")
    int box_price;
    @SerializedName("loose_price")
    int loose_price;
    @SerializedName("basket_price")
    int basket_price;
    @SerializedName("weights")
    String weights;
    @SerializedName("amounts")
    String amounts;
    Boolean selected = false;

    public int getKg_price() {
        return kg_price;
    }

    public void setKg_price(int kg_price) {
        this.kg_price = kg_price;
    }

    public int getDozen_price() {
        return dozen_price;
    }

    public void setDozen_price(int dozen_price) {
        this.dozen_price = dozen_price;
    }

    public int getPiece_price() {
        return piece_price;
    }

    public void setPiece_price(int piece_price) {
        this.piece_price = piece_price;
    }

    public int getBox_price() {
        return box_price;
    }

    public void setBox_price(int box_price) {
        this.box_price = box_price;
    }

    public int getLoose_price() {
        return loose_price;
    }

    public void setLoose_price(int loose_price) {
        this.loose_price = loose_price;
    }

    public int getBasket_price() {
        return basket_price;
    }

    public void setBasket_price(int basket_price) {
        this.basket_price = basket_price;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}

package com.teckzy.msrsilverhouse.Pojo;

import com.google.gson.annotations.SerializedName;

public class SubCategoryPojo
{
    @SerializedName("sub_cat_id")
    int subCatId;
    @SerializedName("sub_cat_name")
    String subCatName;
    @SerializedName("sub_cat_image")
    String subCatImage;
    Boolean selected = false;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatImage() {
        return subCatImage;
    }

    public void setSubCatImage(String subCatImage) {
        this.subCatImage = subCatImage;
    }
}

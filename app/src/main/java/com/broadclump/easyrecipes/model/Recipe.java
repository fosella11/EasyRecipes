package com.broadclump.easyrecipes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Recipe Model.
 */
public class Recipe {

    @SerializedName("title")
    private String mTitle;
    @SerializedName("href")
    private String mHref;
    @SerializedName("ingredients")
    private String mIngredients;
    @SerializedName("thumbnail")
    private String mThumbnail;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String mHref) {
        this.mHref = mHref;
    }

    public String getIngredients() {
        return mIngredients;
    }

    public void setIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

}

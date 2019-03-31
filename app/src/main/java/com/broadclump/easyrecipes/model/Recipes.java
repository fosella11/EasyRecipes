package com.broadclump.easyrecipes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Recipe List Model
 */
public class Recipes {

    @SerializedName("title")
    private String mTitle;
    @SerializedName("version")
    private float mVersion;
    @SerializedName("href")
    private String mHref;
    @SerializedName("results")
    private List<Recipe> mResults;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public float getVersion() {
        return mVersion;
    }

    public void setVersion(float version) {
        this.mVersion = version;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String mHref) {
        this.mHref = mHref;
    }

    public List<Recipe> getResults() {
        return mResults;
    }

    public void setResults(List<Recipe> mResults) {
        this.mResults = mResults;
    }
}

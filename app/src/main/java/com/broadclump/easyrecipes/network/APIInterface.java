package com.broadclump.easyrecipes.network;

import com.broadclump.easyrecipes.model.Recipes;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for API.
 */
public interface APIInterface {

    @GET("api/")
    Call<Recipes> getRecipes(@Query("q") @NonNull String searchText);
}

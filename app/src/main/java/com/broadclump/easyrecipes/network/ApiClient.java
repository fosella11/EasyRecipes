package com.broadclump.easyrecipes.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation API Client (Singleton).
 */
public class ApiClient {

    public static final String URL_BASE= "http://www.recipepuppy.com/";
    private static Retrofit retrofit;

    /**
     * This method will return the retrofit always the same Retrofit
     * instance.
     */
    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
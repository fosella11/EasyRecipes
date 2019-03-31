package com.broadclump.easyrecipes.viewModel;

import com.broadclump.easyrecipes.model.Recipes;
import com.broadclump.easyrecipes.network.APIInterface;
import com.broadclump.easyrecipes.network.ApiClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Implementation ViewModel.
 */
public class RecipesViewModel extends ViewModel {

    private MutableLiveData<Recipes> mRecipesMutableLiveData;

    /**
     * Method to initialize the LiveData and always return the
     * same instance.
     */
    public void init() {
        // The ViewModel will be created per Activity/Fragment
        // The info on the component won't change
        if(this.mRecipesMutableLiveData != null) {
            return;
        }
        mRecipesMutableLiveData = new MutableLiveData<Recipes>();
        //we will load it asynchronously from server in this method
    }

    public MutableLiveData<Recipes> getmRecipesMutableLiveData(){
        return this.mRecipesMutableLiveData;
    }

    /**
     *This method will be used to do new request API
     * and change the data on mutableLiveData.
     *
     * @param query used to perform new API request.
     */
    private void requestToLoadRecipes(String query) {

        Call<Recipes> call =
                ApiClient.getRetrofitInstance().create(APIInterface.class).getRecipes(query);

        call.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                mRecipesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {

            }
        });

    }

    /**
     *This method will be used to do new request API
     * and change the data on mutableLiveData.
     *
     * @param query used to perform new API request.
     */
    public void performSearch(@NonNull String query){
        if(!query.isEmpty()){
            requestToLoadRecipes(query);
        }
    }

}

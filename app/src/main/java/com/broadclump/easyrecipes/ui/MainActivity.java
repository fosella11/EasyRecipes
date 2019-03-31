package com.broadclump.easyrecipes.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.broadclump.easyrecipes.R;
import com.broadclump.easyrecipes.model.Recipe;
import com.broadclump.easyrecipes.model.Recipes;
import com.broadclump.easyrecipes.viewModel.RecipesViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecipesAdapter mRecipeAdapter;
    private Recipes mRecipeList;
    private RecipesViewModel mRecipeViewModel;
    private EditText mSerchEdiText;
    private TextView mNoRecipesOnSearch;

    public static final String EXTRA_TITTLE = "RECIPE_TITTLE";
    public static final String EXTRA_INGRDIENTS = "RECIPE_INGREDIENTS";
    public static final String EXTRA_REFERENCE = "RECIPE_REFERENCE";
    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSerchEdiText = (EditText) findViewById(R.id.serch_edi_text);
        mNoRecipesOnSearch = (TextView) findViewById(R.id.textNoRecipes);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main_activity);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mRecipeViewModel.init();
        mRecipeViewModel.getmRecipesMutableLiveData()
                .observe(this, new Observer<Recipes>() {
                    @Override
                    public void onChanged(Recipes recipes) {
                        mRecipeList = recipes;
                        mRecipeAdapter = new RecipesAdapter(MainActivity.this, mRecipeList);
                        mRecyclerView.setAdapter(mRecipeAdapter);
                        mRecipeAdapter.notifyDataSetChanged();
                    }
                });

        mSerchEdiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRecipeViewModel.performSearch(s.toString());
                if(s.length() > 0){
                    mNoRecipesOnSearch.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * Method to open new Activity DetailsDetails
     * @param recipe Model to get recipe info.
     */
    public void openDetailsActivity(Recipe recipe){
        Intent intent = new Intent(MainActivity.this, RecipeDetails.class);
        intent.putExtra(EXTRA_TITTLE, recipe.getTitle());
        intent.putExtra(EXTRA_INGRDIENTS, recipe.getIngredients());
        intent.putExtra(EXTRA_REFERENCE, recipe.getHref());
        intent.putExtra(EXTRA_IMAGE_URL, recipe.getThumbnail());
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                MainActivity.this).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSerchEdiText.length() > 0){
            mNoRecipesOnSearch.setVisibility(View.GONE);
        }
    }
}

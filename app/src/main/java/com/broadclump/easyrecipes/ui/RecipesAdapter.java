package com.broadclump.easyrecipes.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.broadclump.easyrecipes.R;
import com.broadclump.easyrecipes.model.Recipe;
import com.broadclump.easyrecipes.model.Recipes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Recipe Adapter
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private Context mContext;
    private Recipes recipeList;

    public RecipesAdapter(Context context, Recipes recipeList) {
        this.mContext = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {
        final Recipe recipe = recipeList.getResults().get(position);
        Glide.with(mContext)
                .applyDefaultRequestOptions(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background))
                .load(recipe.getThumbnail())
                .into(holder.recipeImageView);
        holder.recipeTittle.setText(recipe.getTitle());
        holder.recipeSubText.setText(recipe.getIngredients());
        holder.recipeRef.setText(recipe.getHref());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)holder.itemView.getContext()).openDetailsActivity(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.getResults().size();
    }

    /**
     * Pattern ViewHolder to improve the process on List
     * and reduce the code.
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView recipeImageView;
        private TextView recipeTittle;
        private TextView recipeSubText;
        private TextView recipeRef;

        public RecipeViewHolder(View itemView){
            super(itemView);

            recipeImageView = itemView.findViewById(R.id.recipe_card_image);
            recipeTittle = itemView.findViewById(R.id.recipe_card_tittle);
            recipeSubText = itemView.findViewById(R.id.recipe_card_sub);
            recipeRef = itemView.findViewById(R.id.recipe_card_link);
        }
    }
}

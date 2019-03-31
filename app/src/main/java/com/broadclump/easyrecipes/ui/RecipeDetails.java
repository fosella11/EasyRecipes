package com.broadclump.easyrecipes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.broadclump.easyrecipes.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Activity RecipeDetails.
 */
public class RecipeDetails extends AppCompatActivity {

    private TextView mTextTittle;
    private TextView mTextIngredients;
    private TextView mTextReference;
    private ImageView mImageRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        final Intent intent = getIntent();

        mTextTittle = (TextView) findViewById(R.id.detailsTextTittle);
        mTextIngredients = (TextView) findViewById(R.id.detailsTextIngredientsSecond);
        mTextReference = (TextView) findViewById(R.id.detailsTextReferenceSecond);
        mImageRecipe = (ImageView) findViewById(R.id.imageView);

        if(intent != null) {
            mTextTittle.setText(intent.getStringExtra(MainActivity.EXTRA_TITTLE));
            mTextIngredients.setText(intent.getStringExtra(MainActivity.EXTRA_INGRDIENTS));
            mTextReference.setText(intent.getStringExtra(MainActivity.EXTRA_REFERENCE));

            Glide.with(getApplicationContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background))
                    .load(intent.getStringExtra(MainActivity.EXTRA_IMAGE_URL))
                    .into(mImageRecipe);

            mTextReference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = intent.getStringExtra(MainActivity.EXTRA_REFERENCE);
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                }
            });
        }else{
            finish();
        }
    }
}

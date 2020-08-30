package app.jade.ingredient_identifier.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import app.jade.ingredient_identifier.R;
import app.jade.ingredient_identifier.model.Recipe;
import app.jade.ingredient_identifier.services.Yummly;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetRecipesActivity extends AppCompatActivity {
    private static final String IMAGE_LABEL = "imageLabel";
    private String ingredient;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private RecyclerView recipeListRecyclerView;
    private RecipeListAdapter recipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipes);

        recipeListRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe_list);
        ingredient = getIntent().getStringExtra(IMAGE_LABEL);


        Yummly.searchRecipes(ingredient, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                recipes = Yummly.processResults(response);

                GetRecipesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GetRecipesActivity.this);
                        recipeListRecyclerView.setLayoutManager(layoutManager);
                        recipeListAdapter = new RecipeListAdapter(GetRecipesActivity.this, recipes);
                        recipeListRecyclerView.setAdapter(recipeListAdapter);
//                        for (Recipe recipe : recipes) {
//                            Toast.makeText(GetRecipesActivity.this, recipe.toString()
//                                    , Toast.LENGTH_SHORT).show();
//                        }

                    }
                });
            }
        });
    }

    public static Intent makeLaunchIntent(Context context, String imageLabel) {
        Intent intent = new Intent(context, GetRecipesActivity.class);
        intent.putExtra(IMAGE_LABEL, imageLabel);
        return intent;
    }
}

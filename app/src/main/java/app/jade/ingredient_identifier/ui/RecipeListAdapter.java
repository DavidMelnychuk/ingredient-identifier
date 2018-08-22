package app.jade.ingredient_identifier.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import app.jade.ingredient_identifier.R;
import app.jade.ingredient_identifier.model.Recipe;

public class RecipeListAdapter extends RecyclerView.Adapter {
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private Context context;


    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipeList = recipes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item,
                parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecipeViewHolder) holder).bind(recipeList.get(position));
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;
        TextView recipeRating;
        TextView recipeSource;
        ImageView recipeImage;

        RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            recipeRating = (TextView) itemView.findViewById(R.id.tv_recipe_rating);
            recipeSource = (TextView) itemView.findViewById(R.id.tv_recipe_source);
            recipeImage = (ImageView) itemView.findViewById(R.id.iv_recipe_image);

        }

        void bind(Recipe recipe) {
            recipeName.setText(recipe.getRecipeName());
            recipeRating.setText("Rating: " + recipe.getRating() + " out of 5");
            recipeSource.setText("Made by " + recipe.getSource());
            Picasso.get().load(recipe.getImageUrl()).into(recipeImage);
        }
    }


}

package app.jade.ingredient_identifier.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.jade.ingredient_identifier.model.Recipe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Yummly {
    private static final String API_KEY = "d2e2064339d1ce55e5075c8008333792";
    private static final String APP_ID = "f6ccf80f";
    private static final String BASE_URL = "http://api.yummly.com/v1/api/recipes?";
    private static final String SEARCH_QUERY_INGREDIENT = "allowedIngredient[]";
    private static final String API_ID_QUERY_PARAMETER = "X-Yummly-App-ID";
    private static final String API_KEY_QUERY_PARAMETER = "X-Yummly-App-Key";


    public static void searchRecipes(String ingredient, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(SEARCH_QUERY_INGREDIENT, ingredient);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header(API_ID_QUERY_PARAMETER, APP_ID)
                .header(API_KEY_QUERY_PARAMETER, API_KEY)
                .build();

        Log.d("url", url);

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static ArrayList<Recipe> processResults(Response response) {
        ArrayList<Recipe> recipes = new ArrayList();

        try {
            if (response.isSuccessful()) {
                String jsonData = response.body().string();

                JSONObject yummlyJSON = new JSONObject(jsonData);
                JSONArray matchesJSON = yummlyJSON.getJSONArray("matches");
                for (int i = 0; i < matchesJSON.length(); i++) {
                    JSONObject recipeJSON = matchesJSON.getJSONObject(i);
                    String recipeName = recipeJSON.getString("recipeName");

                    ArrayList<String> ingredients = new ArrayList();
                    JSONArray ingredientsJSON = recipeJSON.getJSONArray("ingredients");

                    for (int j = 0; j < ingredientsJSON.length(); j++) {
                        ingredients.add(ingredientsJSON.get(j).toString());
                    }

                    String imageUrl = recipeJSON.getJSONObject("imageUrlsBySize").getString("90");
                    String rating = recipeJSON.getString("rating");
                    String source = recipeJSON.getString("sourceDisplayName");
                    String id = recipeJSON.getString("id");

                    Recipe recipe = new Recipe(id, recipeName, ingredients, imageUrl, rating, source);
                    recipes.add(recipe);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}

package app.jade.ingredient_identifier;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GetRecipesActivity extends AppCompatActivity {
    private static final String IMAGE_LABEL = "imageLabel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipes);
    }

    public static Intent makeLaunchIntent(Context context, String imageLabel) {
        Intent intent = new Intent(context, GetRecipesActivity.class);
        intent.putExtra(IMAGE_LABEL, imageLabel);
        return intent;
    }
}

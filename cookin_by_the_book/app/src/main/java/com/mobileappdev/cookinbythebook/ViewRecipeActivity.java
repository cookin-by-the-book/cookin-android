package com.mobileappdev.cookinbythebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewRecipeActivity extends AppCompatActivity {

    static final private String TAG = "ViewRecipeActivity";

    TextView recipeName;
    TextView prepTime;
    TextView cookTime;
    TextView servings;
    ListView ingredientsListView;
    ListView stepsListView;
    TextView notes;
    TextView categories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe);
        Bundle data = getIntent().getExtras();
        Recipe incame = (Recipe) data.get("recipe");

        // set the things to the thing
        recipeName = (TextView) findViewById(R.id.staticRecipeName);
        prepTime = (TextView) findViewById(R.id.staticPrepTime);
        cookTime = (TextView) findViewById(R.id.staticCookTime);
        servings = (TextView) findViewById(R.id.staticServings);
        ingredientsListView = (ListView) findViewById(R.id.staticIngredientsListView);
        stepsListView = (ListView) findViewById(R.id.staticStepsListView);
        notes = (TextView) findViewById(R.id.staticNotesTextMultiLine);
        categories = (TextView) findViewById(R.id.staticCategoriesText);

        // set the TextViews to what they should be
        recipeName.setText(incame.name);
        prepTime.setText("Prep:\n" + incame.prepTime);
        cookTime.setText("Cook:\n" + incame.cookTime);
        servings.setText("Serves:\n" + incame.servings);
        notes.setText(incame.notes);


        Log.d(TAG, incame.toString());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
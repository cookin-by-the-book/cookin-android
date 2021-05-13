package com.mobileappdev.cookinbythebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

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

    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    ArrayList<Step> stepArrayList = new ArrayList<>();



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
        recipeName.setText(incame.name + " by " + incame.owner);
        prepTime.setText("Prep time:\n" + incame.prepTime + " mins");
        cookTime.setText("Cook time:\n" + incame.cookTime + " mins");
        servings.setText("Serves:\n" + incame.servings);
        if (incame.notes.isEmpty()){
            notes.setText(("None"));
        } else {
            notes.setText(incame.notes);
        }



        // ingredient map to ListArray<ingredient>

        for (Map.Entry<String, String> ingredient : incame.ingredients.entrySet()) {
            ingredientArrayList.add(new Ingredient(ingredient.getKey(), ingredient.getValue()));
        }

        for (String step : incame.steps) {
            stepArrayList.add(new Step(step));
        }



        // set the list views
        EditableIngredientArrayAdapter staticIngredientArrayAdapter = new EditableIngredientArrayAdapter(this, R.layout.editable_ingredient_item, ingredientArrayList);
        ingredientsListView.setAdapter(staticIngredientArrayAdapter);

        EditableStepArrayAdapter staticStepArrayAdapter = new EditableStepArrayAdapter(this, R.layout.editable_step_item, stepArrayList);
        stepsListView.setAdapter(staticStepArrayAdapter);

        // set the categories
        if (incame.category != null) {
            categories.setText(String.join(",", incame.category));
        } else {
            categories.setText("None");
        }



        Log.d(TAG, incame.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences globalSettingsReader = (((App) this.getApplication()).preferences);

        getMenuInflater().inflate(R.menu.share_recipe, menu);
        return super.onCreateOptionsMenu(menu);
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
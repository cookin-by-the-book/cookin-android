package com.mobileappdev.cookinbythebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
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
    ImageView recipePicture;
    Databaser db = new Databaser();
    Recipe incame;

    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    ArrayList<Step> stepArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe);

        Bundle data = getIntent().getExtras();
        incame = (Recipe) data.get("recipe");
        Log.d(TAG, "please fucking work: " + incame.getUuidOhMyGod());

        // set the things to the thing
        recipeName = (TextView) findViewById(R.id.staticRecipeName);
        prepTime = (TextView) findViewById(R.id.staticPrepTime);
        cookTime = (TextView) findViewById(R.id.staticCookTime);
        servings = (TextView) findViewById(R.id.staticServings);
        ingredientsListView = (ListView) findViewById(R.id.staticIngredientsListView);
        stepsListView = (ListView) findViewById(R.id.staticStepsListView);
        notes = (TextView) findViewById(R.id.staticNotesTextMultiLine);
        categories = (TextView) findViewById(R.id.staticCategoriesText);
        recipePicture = (ImageView) findViewById(R.id.staticRecipePhoto);

        db.init();
        // set the picture?
        if (!incame.picture.isEmpty()) {
            byte[] decodedString = android.util.Base64.decode(incame.picture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            recipePicture.setImageBitmap(decodedByte);
        }


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
        MenuItem shareButton = menu.findItem(R.id.action_share_recipe);
        shareButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "SHARE BUTTON PUSHED");
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewRecipeActivity.this);
                builder.setTitle("Email to share with:");
                final EditText input = new EditText(ViewRecipeActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);
                builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailToShareWith = input.getText().toString();
                        Log.d(TAG, emailToShareWith);
                        shareWith(emailToShareWith);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
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

    private void shareWith(String email) {
        db.getStore("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // determine if the email is an email in the database or not...
                    boolean recipeWasShared = false;
                    for (QueryDocumentSnapshot document0 : task.getResult()) {
                        Map<String, Object> parsed = document0.getData();
                        // if the email matches!
                        if (email.equals((String) parsed.get("email"))) {
                            recipeWasShared = true;
                            Log.d(TAG, "got something");
                            db.getStore("users").document(document0.getId()).update("shared", FieldValue.arrayUnion(incame.getUuidOhMyGod()));
                            Toast.makeText(ViewRecipeActivity.this, "Shared Recipe!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if(!recipeWasShared) {
                        Toast.makeText(ViewRecipeActivity.this, "User with that email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}


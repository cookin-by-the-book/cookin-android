//package com.mobileappdev.cookinbythebook;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
//public class BuildRecipeListActivity extends Activity {
//    private static final String TAG = "BuildRecipeListActivity";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_home);
//        Log.d(TAG, "onCreate: Started");
//        ListView mListView = (ListView) findViewById(R.id.homeListView);
//
//        // recipe objects
//        Recipe sandwich = new Recipe("sandwich", "Mom");
//        Recipe cake = new Recipe("cake", "John");
//
//        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
//        recipeArrayList.add(sandwich);
//        recipeArrayList.add(cake);
//
//        RecipeArrayAdapter adapter = new RecipeArrayAdapter(this, R.layout.recipe_item, recipeArrayList);
//        mListView.setAdapter(adapter);
//    }
//}

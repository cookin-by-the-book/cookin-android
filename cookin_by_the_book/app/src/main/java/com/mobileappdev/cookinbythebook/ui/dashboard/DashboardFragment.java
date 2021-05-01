package com.mobileappdev.cookinbythebook.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileappdev.cookinbythebook.Databaser;
import com.mobileappdev.cookinbythebook.MainActivity;
import com.mobileappdev.cookinbythebook.R;
import com.mobileappdev.cookinbythebook.Recipe;
import com.mobileappdev.cookinbythebook.RecipeArrayAdapter;
import com.mobileappdev.cookinbythebook.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.app.ActivityCompat.recreate;

public class DashboardFragment extends Fragment {
    private String spinnerVal;
    private int check = 0;
    private String last;

    private DashboardViewModel dashboardViewModel;

    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        setHasOptionsMenu(true);

        ListView mListView = (ListView) root.findViewById((R.id.homeListView));
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        ArrayList<Recipe> allRecipesArrayList = new ArrayList<>();


        // recipe objects (here is where we would query the DB)
        Spinner spinner = (Spinner) root.findViewById(R.id.filter_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.filters_array, android.R.layout.simple_spinner_item);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Code in this listener adapted from https://stackoverflow.com/a/28466880
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinnerVal = spinner.getSelectedItem().toString();
                if (spinnerVal != last) {
                    last = spinnerVal;
                    // Fragment reload code from https://stackoverflow.com/a/44299677
                    FragmentTransaction ftr = getFragmentManager().beginTransaction();
                    ftr.detach(DashboardFragment.this).attach(DashboardFragment.this).commit();
                    Log.d(TAG, spinnerVal);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        String hcname = null;
        String hcowner = null;
        String hcpicture = null;
        Map<String, String> hcingredients = null;
        String hcnotes = null;
        ArrayList<String> hcsharedWith = null;
        ArrayList<String> hcsteps = null;
        ArrayList<String> hcfavorited = null;

        String userID = "RdaBZx60uESOJrIxUnQV";
        String userName = "Matthew";

        Databaser db = new Databaser();
        db.init();
        CollectionReference recipeStore = db.getStore("recipes");
        // this is async, so it's sorta annoying
        recipeStore.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> dater = document.getData();
                                String name = (String) dater.get("name");
                                // todo get actual name from UUID
                                String owner = (String) dater.get("owner");
                                String picture = (String) dater.get("picture");
                                Map<String, String> ingredients = (Map<String, String>) dater.get("ingredients");
                                String notes = (String) dater.get("notes");
                                ArrayList<String> sharedWith = (ArrayList<String>) dater.get("shared_with");
                                ArrayList<String> steps = (ArrayList<String>) dater.get("steps");
                                ArrayList<String> favorited = (ArrayList<String>) dater.get("favorited");

                                Recipe incoming = new Recipe(name, owner, picture, ingredients, notes, sharedWith, steps, favorited);
                                allRecipesArrayList.add(incoming);
                                //recipeArrayList.add(incoming);
                                //Log.d(TAG, incoming.toString());
                            }
                            RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
                            Log.d(TAG, "SPINNERVAL:");
                            Log.d(TAG, spinnerVal);
                            if (spinnerVal.equals("Favorites")) {
                                Log.d(TAG, "Favorites IF");
                                for (int counter = 0; counter < allRecipesArrayList.size(); counter++) {
                                    if (allRecipesArrayList.get(counter).favorited.contains(userID)) {
                                        recipeArrayList.add(allRecipesArrayList.get(counter));
                                    }
                                }
                            }
                            else if (spinnerVal.equals("My Recipes")) {
                                Log.d(TAG, "My Recipes IF");
                                for (int counter = 0; counter < allRecipesArrayList.size(); counter++) {
                                    if (allRecipesArrayList.get(counter).owner.equals(userID)) {
                                        recipeArrayList.add(allRecipesArrayList.get(counter));
                                    }
                                }
                            }
                            else {
                                Log.d(TAG, "ELSE IF");
                                for (int counter = 0; counter < allRecipesArrayList.size(); counter++) {
                                    recipeArrayList.add(allRecipesArrayList.get(counter));
                                    }
                            }
                            Collections.sort(recipeArrayList, new Comparator<Recipe>() {
                                @Override
                                public int compare(Recipe r1, Recipe r2) {
                                    return (r1.name.toLowerCase()).compareTo(r2.name.toLowerCase());
                                }
                            });

                            mListView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                            RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);

                            if (spinnerVal.equals("Favorites")) {
                                for (int counter = 0; counter < allRecipesArrayList.size(); counter++) {
                                    if (allRecipesArrayList.get(counter).favorited.contains(userID)) {
                                        recipeArrayList.add(allRecipesArrayList.get(counter));
                                    }
                                }
                            }
                            else if (spinnerVal.equals("My Recipes")) {
                                Log.d(TAG, "My Recipes IF");
                                for (int counter = 0; counter < allRecipesArrayList.size(); counter++) {
                                    if (allRecipesArrayList.get(counter).owner.equals(userID)) {
                                        recipeArrayList.add(allRecipesArrayList.get(counter));
                                    }
                                }
                            }
                            else {
                                for (int counter = 0; counter < allRecipesArrayList.size(); counter++) {
                                    recipeArrayList.add(allRecipesArrayList.get(counter));
                                }
                            }

                            Collections.sort(recipeArrayList, new Comparator<Recipe>() {
                                @Override
                                public int compare(Recipe r1, Recipe r2) {
                                    return (r1.name.toLowerCase()).compareTo(r2.name.toLowerCase());
                                }
                            });

                            mListView.setAdapter(adapter);
                        }
                    }
                });
        Log.d(TAG, "onCreateView completed");
        return root;
    }

    // Based on answers to https://stackoverflow.com/q/34291453
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        Context mContext = getContext();
        SearchView searchView = new SearchView(((MainActivity) mContext).getSupportActionBar().getThemedContext());

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);

        item.setActionView(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
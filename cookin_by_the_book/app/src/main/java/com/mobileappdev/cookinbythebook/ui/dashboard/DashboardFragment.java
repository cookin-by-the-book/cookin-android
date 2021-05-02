package com.mobileappdev.cookinbythebook.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.mobileappdev.cookinbythebook.App;
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
import java.util.Set;

import static androidx.core.app.ActivityCompat.recreate;

public class DashboardFragment extends Fragment {
    private String spinnerVal;
    ObservableInteger obsInt = new ObservableInteger();
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
                    Log.d(TAG, "RELOADING");
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

        obsInt.setOnIntegerChangeListener(new OnIntegerChangeListener()
        {
            @Override
            public void onIntegerChanged(int newValue)
            {
                Log.d(TAG, "LISTENER LISTENED!!!");
                FragmentTransaction ftr = getFragmentManager().beginTransaction();
                ftr.detach(DashboardFragment.this).attach(DashboardFragment.this).commit();
            }
        });

        String search = obsInt.getSearch();
        String userID = "RdaBZx60uESOJrIxUnQV";
        String user = "Charlie";

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
                                String picture = (String) dater.get("picture");
                                Map<String, String> ingredients = (Map<String, String>) dater.get("ingredients");
                                String notes = (String) dater.get("notes");
                                ArrayList<String> sharedWith = (ArrayList<String>) dater.get("shared_with");
                                ArrayList<String> steps = (ArrayList<String>) dater.get("steps");
                                ArrayList<String> favorited = (ArrayList<String>) dater.get("favorited");
                                ArrayList<String> categories = (ArrayList<String>) dater.get("categories");
                                db.getName((String)dater.get("owner"), new Databaser.UserCallback() {
                                    @Override
                                    public void onCallback(ArrayList<String> userName) {
                                        String owner = userName.get(0);
                                        Recipe incoming = new Recipe(name, owner, picture, ingredients, notes, sharedWith, steps, favorited, categories);
                                        RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);

                                        Log.d(TAG, search);
                                        Log.d(TAG, incoming.name);

                                        if (spinnerVal.equals("Ingredients")) {
                                            Set<Map.Entry<String, String>> s = incoming.ingredients.entrySet();
                                            for (Map.Entry<String, String> it: s) {
                                                if (it.getKey().toLowerCase().contains(search)) {
                                                    if (!recipeArrayList.contains(incoming)) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                }
                                            }
                                        }
                                        else if (spinnerVal.equals("Category")) {
                                            Log.d(TAG, search);
                                            for (int counter = 0; counter < incoming.category.size(); counter++) {
                                                Log.d(TAG, incoming.category.get(counter));
                                                if (incoming.category.get(counter).toLowerCase().contains(search)) {
                                                    if (!recipeArrayList.contains(incoming)) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            if (incoming.name.toLowerCase().contains(search)) {
                                                if (spinnerVal.equals("Favorites")) {
                                                    if (incoming.favorited.contains(userID)) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                } else if (spinnerVal.equals("My Recipes")) {
                                                    if (incoming.owner.equals(user)) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                } else if (spinnerVal.equals("Shared with me")) {
                                                    if (!incoming.owner.equals(user)) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                } else {
                                                    recipeArrayList.add(incoming);
                                                }
                                            }
                                        }

                                        Collections.sort(recipeArrayList,new Comparator<Recipe>() {
                                            @Override
                                            public int compare (Recipe r1, Recipe r2){
                                                return (r1.name.toLowerCase()).compareTo(r2.name.toLowerCase());
                                            }
                                        });
                                        mListView.setAdapter(adapter);
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                            RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
                            mListView.setAdapter(adapter);
                        }
                    }
                });

        SharedPreferences globalSettingsReader = (((App) getActivity().getApplication()).preferences);
//        globalSettingsEditor.putString("uuid", "asdfasdf");
//        globalSettingsEditor.commit();
//        Log.d(TAG, db.getName("RdaBZx60uESOJrIxUnQV").toString());
        db.getName("RdaBZx60uESOJrIxUnQV", new Databaser.UserCallback() {
            @Override
            public void onCallback(ArrayList<String> userName) {
                Log.d(TAG, userName.get(0) +  userName.get(1));
            }
        });
        Log.d(TAG, globalSettingsReader.getString("uuid", "0"));
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
                Log.d(TAG, "REACHED onQueryTextSubmit");
                obsInt.set(obsInt.get() + 1);
                obsInt.setSearch(query);
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
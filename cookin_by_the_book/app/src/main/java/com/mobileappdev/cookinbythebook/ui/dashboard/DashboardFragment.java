package com.mobileappdev.cookinbythebook.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private String spinner2Val;
    ObservableInteger obsInt = new ObservableInteger();
    private String last;
    private String last2;

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
        Spinner spinner2 = (Spinner) root.findViewById(R.id.search_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.filters_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> spinner2Adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.search_array, android.R.layout.simple_spinner_item);

        spinner.setAdapter(spinnerAdapter);
        spinner2.setAdapter(spinner2Adapter);

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
                    //Log.d(TAG, spinnerVal);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // Code in this listener adapted from https://stackoverflow.com/a/28466880
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner2Val = spinner2.getSelectedItem().toString();
                if (spinner2Val != last2) {
                    last2 = spinner2Val;
                    // Fragment reload code from https://stackoverflow.com/a/44299677
                    Log.d(TAG, "RELOADING");
                    FragmentTransaction ftr = getFragmentManager().beginTransaction();
                    ftr.detach(DashboardFragment.this).attach(DashboardFragment.this).commit();
                    //Log.d(TAG, spinner2Val);
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
        final String[] userID = {""};
        final String[] userFirst = {""};

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String authEmail = user.getEmail();

        Databaser db = new Databaser();
        db.init();
        CollectionReference recipeStore = db.getStore("recipes");
        CollectionReference userStore = db.getStore("users");

        userStore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task0) {
                if (task0.isSuccessful()) {
                    for (QueryDocumentSnapshot document0 : task0.getResult()) {
                        Map<String, Object> dater0 = document0.getData();
                        String email = (String) dater0.get("email");
                        String firstName = (String) dater0.get("firstName");
                        if (email.equals(authEmail)) {
                            userFirst[0] = firstName;
                            userID[0] = document0.getId();
                        }
                    }
                } else {
                    //Log.d(TAG, "Error getting documents.", task0.getException());
                }
            }
        });

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
                                        Recipe incoming = new Recipe(name, owner, picture, ingredients, notes, sharedWith, steps, favorited, categories, "", "", "");
                                        RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
                                        if (spinner2Val.equals("Ingredients")) {
                                            Set<Map.Entry<String, String>> s = incoming.ingredients.entrySet();
                                            for (Map.Entry<String, String> it: s) {
                                                if (it.getKey().toLowerCase().contains(search)) {
                                                    if (!recipeArrayList.contains(incoming)) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                }
                                            }
                                        }
                                        else if (spinner2Val.equals("Category")) {
                                            for (int counter = 0; counter < incoming.category.size(); counter++) {
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
                                                    if (incoming.favorited.contains(userID[0])) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                } else if (spinnerVal.equals("My Recipes")) {
                                                    if (incoming.owner.equals(userID[0])) {
                                                        recipeArrayList.add(incoming);
                                                    }
                                                } else if (spinnerVal.equals("Shared with me")) {
                                                    if (!incoming.owner.equals(userID[0])) {
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
                            //Log.d(TAG, "Error getting documents.", task.getException());
                            RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
                            mListView.setAdapter(adapter);
                        }
                    }
                });

        SharedPreferences globalSettingsReader = (((App) getActivity().getApplication()).preferences);
        db.getName("RdaBZx60uESOJrIxUnQV", new Databaser.UserCallback() {
            @Override
            public void onCallback(ArrayList<String> userName) {
            }
        });
        return root;
    }

    // Based on answers to https://stackoverflow.com/q/34291453
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //final boolean[] hasChanged = new boolean[1];
        //hasChanged[0] = false;

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
                obsInt.setChanged(1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText) && obsInt.getChanged() == 1) {
                    obsInt.set(obsInt.get() + 1);
                    obsInt.setSearch("");
                    obsInt.setChanged(0);
                }
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
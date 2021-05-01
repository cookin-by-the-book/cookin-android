package com.mobileappdev.cookinbythebook.ui.dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class DashboardFragment extends Fragment {

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

        // recipe objects (here is where we would query the DB)

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
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // doesn't work
        Log.d(TAG, " shit " + db.getOwner("1OZ1hfQzm7mHwKrujHNC"));


        Recipe sandwich = new Recipe("sandwich", "Mom");
        Recipe cake = new Recipe("cake", "John");
        Recipe pasta = new Recipe("Pasta", "Matthew");
        Recipe carbonara = new Recipe("Carbonara", "Jean");
        Recipe hamburger = new Recipe("hamburger", "Jerry");

        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);
        recipeArrayList.add(pasta);
        recipeArrayList.add(carbonara);
        recipeArrayList.add(hamburger);

        Collections.sort(recipeArrayList, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                return (r1.name.toLowerCase()).compareTo(r2.name.toLowerCase());
            }
        });

        RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
        mListView.setAdapter(adapter);
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
        Spinner spinner = (Spinner) getView().findViewById(R.id.filter_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.filters_array, android.R.layout.simple_spinner_item);

        spinner.setAdapter(adapter);

    }
}
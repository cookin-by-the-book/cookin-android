package com.mobileappdev.cookinbythebook.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileappdev.cookinbythebook.Databaser;
import com.mobileappdev.cookinbythebook.R;
import com.mobileappdev.cookinbythebook.Recipe;
import com.mobileappdev.cookinbythebook.RecipeArrayAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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

        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);        recipeArrayList.add(sandwich);
        recipeArrayList.add(cake);

        RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
        mListView.setAdapter(adapter);
        Log.d(TAG, "onCreateView completed");
        return root;
    }
}
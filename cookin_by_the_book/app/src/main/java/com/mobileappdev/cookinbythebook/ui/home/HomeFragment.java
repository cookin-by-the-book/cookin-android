package com.mobileappdev.cookinbythebook.ui.home;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.mobileappdev.cookinbythebook.R;
import com.mobileappdev.cookinbythebook.Recipe;
import com.mobileappdev.cookinbythebook.RecipeArrayAdapter;
import com.mobileappdev.cookinbythebook.ViewRecipeActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private static final String TAG = "HomeFragment";

    private FirebaseAuth mAuth;



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
                                Map<String, Object> dater = document.getData();
                                String name = (String) dater.get("name");
                                String picture = (String) dater.get("picture");
                                Map<String, String> ingredients = (Map<String, String>) dater.get("ingredients");
                                String notes = (String) dater.get("notes");
                                ArrayList<String> sharedWith = (ArrayList<String>) dater.get("shared_with");
                                ArrayList<String> steps = (ArrayList<String>) dater.get("steps");
                                ArrayList<String> favorited = (ArrayList<String>) dater.get("favorited");
                                ArrayList<String> categories = (ArrayList<String>) dater.get("categories");

                                // get human name from user id
                                db.getName((String) dater.get("owner"), new Databaser.UserCallback() {
                                    @Override
                                    public void onCallback(ArrayList<String> userName) {
                                        // i think we have to move everything INSIDE this...
                                        String owner = userName.get(0);
                                        Log.d(TAG, "App Owner:");
                                        Log.d(TAG, owner);
                                        //String owner = userName.get(0);
                                        Recipe incoming = new Recipe(name, owner, picture, ingredients, notes, sharedWith, steps, favorited, categories, "", "", "");
                                        recipeArrayList.add(incoming);
                                        RecipeArrayAdapter adapter = new RecipeArrayAdapter(getContext(), R.layout.recipe_item, recipeArrayList);
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


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ViewRecipeActivity.class);
                Recipe intentRecipe = recipeArrayList.get(position);
                Log.d(TAG, intentRecipe.toString());
                intent.putExtra("recipe", intentRecipe);
                startActivity(intent);
            }
        });
        // demo on how to get & set global prefs


        // set the global userId whenever they visit this page... it's messy, but it should work
        // and save time on the addRecipe page

//        SharedPreferences.Editor globalSettingsEditor = ((App)getActivity().getApplication()).preferences.edit();
//        SharedPreferences globalSettingsReader = (((App) getActivity().getApplication()).preferences);
//        FirebaseUser user = mAuth.getCurrentUser();
//        String currentUserEmail = user.getEmail();

        //
//        db.getStore("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String, Object> incomingUser = document.getData();
//                                String incomingEmail = (String) incomingUser.get("email");
//
//                                if (incomingEmail.equals(currentUserEmail)) {
//                                    globalSettingsEditor.putString("uuid", document.getId());
//                                    globalSettingsEditor.commit();
//                                    Log.d(TAG, "found user id?");
//                                    break;
//                                }
//                            }
//                            Log.d(TAG, globalSettingsReader.getString("uuid", "0"));
//
//                        } else {
//                            Toast.makeText(getContext(), "This should never appear", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
        //
//                globalSettingsEditor.putString("uuid", "asdfasdf");
//        globalSettingsEditor.commit();
//        Log.d(TAG, db.getName("RdaBZx60uESOJrIxUnQV").toString());
//        db.getName("RdaBZx60uESOJrIxUnQV", new Databaser.UserCallback() {
//            @Override
//            public void onCallback(ArrayList<String> userName) {
//                Log.d(TAG, userName.get(0) +  userName.get(1));
//            }
//        });
//        Log.d(TAG, globalSettingsReader.getString("uuid", "0"));
        Log.d(TAG, "onCreateView completed");
        return root;
    }
}
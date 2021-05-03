package com.mobileappdev.cookinbythebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link navigation_add_recipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class navigation_add_recipe extends Fragment {

    private static final String TAG = "NewRecipePage";

    private ImageButton newIngredientsButton, newStepButton;
    private ListView ingredientsListView, stepsListView;
    public ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    public ArrayList<Step> stepArrayList = new ArrayList<>();
    public EditText recipeName, prepTime, cookTime, servings, notes, categories;
    private User user;
    private Databaser db;
    FirebaseAuth mAuth;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public navigation_add_recipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment navigation_add_recipe.
     */
    // TODO: Rename and change types and number of parameters
    public static navigation_add_recipe newInstance(String param1, String param2) {
        navigation_add_recipe fragment = new navigation_add_recipe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d(TAG, "done");
        View root = inflater.inflate(R.layout.fragment_navigation_add_recipe, container, false);

        // show the checkmark button
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();

        newIngredientsButton = (ImageButton) root.findViewById(R.id.editAddIngredientsButton);
        newStepButton = (ImageButton) root.findViewById(R.id.editAddStepsButton);
        ingredientsListView = (ListView) root.findViewById(R.id.editIngredientsListView);
        stepsListView = (ListView) root.findViewById(R.id.editStepsListView);

        recipeName = (EditText) root.findViewById(R.id.editRecipeName);
        prepTime = (EditText) root.findViewById(R.id.editPrepTime);
        cookTime = (EditText) root.findViewById(R.id.editCookTime);
        servings = (EditText) root.findViewById(R.id.editServings);
        notes = (EditText) root.findViewById(R.id.editNotesTextMultiLine);
        categories = (EditText) root.findViewById(R.id.editCategoriesText);


        db = new Databaser();
        db.init();

        SharedPreferences.Editor globalSettingsEditor = ((App)getActivity().getApplication()).preferences.edit();
        SharedPreferences globalSettingsReader = ((App)getActivity().getApplication()).preferences;

        FirebaseUser user = mAuth.getCurrentUser();
        String currentUserEmail = user.getEmail();

        //
        db.getStore("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> incomingUser = document.getData();
                                String incomingEmail = (String) incomingUser.get("email");
                                if (incomingEmail.equals(currentUserEmail)) {
                                    globalSettingsEditor.putString("uuid", document.getId());
                                    globalSettingsEditor.commit();
                                    break;
                                }
                            }
                            Log.d(TAG, globalSettingsReader.getString("uuid", "0"));

                        } else {
                            Toast.makeText(getContext(), "This should never appear", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        // array adapters for the two custom lists
        EditableIngredientArrayAdapter editableIngredientArrayAdapter = new EditableIngredientArrayAdapter(getContext(), R.layout.editable_ingredient_item, ingredientArrayList);
        ingredientsListView.setAdapter(editableIngredientArrayAdapter);

        EditableStepArrayAdapter editableStepArrayAdapter = new EditableStepArrayAdapter(getContext(), R.layout.editable_step_item, stepArrayList);
        stepsListView.setAdapter(editableStepArrayAdapter);

        newIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ingredient button clicked");
                EditText newIngredientName = (EditText) root.findViewById(R.id.newIngredientName);
                EditText newIngredientQuantity = (EditText) root.findViewById(R.id.newIngredientQuantity);
                ingredientArrayList.add(new Ingredient(newIngredientName.getText().toString(),newIngredientQuantity.getText().toString()));
                newIngredientName.setText("");
                newIngredientQuantity.setText("");
                editableIngredientArrayAdapter.notifyDataSetChanged();
////                ingredientArrayList.add(new Ingredient("", ""));
            }
        });
//
        newStepButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "step button clicked ");
                EditText newStep = (EditText) root.findViewById(R.id.newStep);
                stepArrayList.add(new Step(newStep.getText().toString()));
                newStep.setText("");
                editableStepArrayAdapter.notifyDataSetChanged();
            }
        });


        ingredientsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Do you want to delete this ingredient?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ingredientArrayList.remove(position);
                                editableIngredientArrayAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }
        });

        stepsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Do you want to delete this step?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stepArrayList.remove(position);
                                editableStepArrayAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }
        });



        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.accept_recipe, menu);
        MenuItem item = menu.findItem(R.id.action_accept);

        Context mContext = getContext();

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "WHAT THE FUCK");
                Recipe outgoing = buildOutgoingRecipeObject();
                if (outgoing != null) {
                    sendRecipeToDatabase(outgoing);
                }
                return false;
            }
        });
    }

    private Recipe buildOutgoingRecipeObject() {
        // for reading the UUID later
        SharedPreferences globalSettingsReader = (((App) getActivity().getApplication()).preferences);

        String recipeName = this.recipeName.getText().toString();
        String prepTime = this.prepTime.getText().toString();
        String cookTime = this.cookTime.getText().toString();
        String servings = this.servings.getText().toString();
        ArrayList<Ingredient> ingredientArrayList = this.ingredientArrayList;
        ArrayList<Step> stepArrayList = this.stepArrayList;
        String notes = this.notes.getText().toString();
        String categories = this.categories.getText().toString();

        Map<String, String> ingredientsMap = new HashMap<>();
        ArrayList<String> favorited = new ArrayList<>();
        ArrayList<String> stringStepArrayList = new ArrayList<>();

        // the user UUID...
        String userUUID = globalSettingsReader.getString("uuid", "0");


        // check to make sure everything is filled
        if (recipeName.isEmpty()) {
            Toast.makeText(getContext(), "Missing recipe name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (prepTime.isEmpty()) {
            Toast.makeText(getContext(), "Missing prep time", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (cookTime.isEmpty()) {
            Toast.makeText(getContext(), "Missing cook time", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (servings.isEmpty()) {
            Toast.makeText(getContext(), "Missing servings", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (ingredientArrayList.isEmpty()) {
            Toast.makeText(getContext(), "Missing ingredients", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (stepArrayList.isEmpty()) {
            Toast.makeText(getContext(), "Missing steps", Toast.LENGTH_SHORT).show();
            return null;
        }

        // build the ingredients Map<>
        for (Ingredient ingredient : ingredientArrayList) {
            ingredientsMap.put(ingredient.getName(), ingredient.getQuantity());
        }

        // build tjhe steps ArrayList<String>
        for (Step step : stepArrayList) {
            stringStepArrayList.add(step.getStep());
        }

        // build the category ArrayList<String> // this is jank as f
        String[] categoryArray = categories.split(",");
        List<String> categoryList = Arrays.asList(categoryArray);
        ArrayList<String> category = new ArrayList<String>(categoryList);

        Recipe outgoing = new Recipe(
                recipeName,
                userUUID,
                "",
                ingredientsMap,
                notes,
                new ArrayList<>(),
                stringStepArrayList,
                new ArrayList<>(),
                category,
                prepTime,
                cookTime,
                servings
        );
        Log.d(TAG, outgoing.toString());

        return outgoing;
    }

    private void sendRecipeToDatabase(Recipe outgoing) {
        SharedPreferences globalSettingsReader = (((App) getActivity().getApplication()).preferences);
        db.getStore("recipes").add(outgoing).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                db.getStore("users").document(globalSettingsReader.getString("uuid", "0'"))
                        .update("recipes", FieldValue.arrayUnion(documentReference.getId()));
                Toast.makeText(getContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        // update the users' own recipes
         
    }
}
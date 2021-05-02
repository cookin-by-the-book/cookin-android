package com.mobileappdev.cookinbythebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mobileappdev.cookinbythebook.ui.home.HomeViewModel;

import java.util.ArrayList;

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

        newIngredientsButton = (ImageButton) root.findViewById(R.id.editAddIngredientsButton);
        newStepButton = (ImageButton) root.findViewById(R.id.editAddStepsButton);
        ingredientsListView = (ListView) root.findViewById(R.id.editIngredientsListView);
        stepsListView = (ListView) root.findViewById(R.id.editStepsListView);




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

}
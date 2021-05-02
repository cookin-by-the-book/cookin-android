package com.mobileappdev.cookinbythebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.ViewGroupUtils;

import java.util.ArrayList;

public class EditableIngredientArrayAdapter extends ArrayAdapter<Ingredient> {
    private static final String TAG = "EditableIngredientArrayAdapter";

    private Context mContext;
    int mResource;

    public EditableIngredientArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String quantity = getItem(position).getQuantity();
        Ingredient ingredient = new Ingredient(name, quantity);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvIngredient = (TextView) convertView.findViewById(R.id.inputtedIngredientName);
        TextView tvQuanity = (TextView) convertView.findViewById(R.id.inputtedIngredientQuantity);
        tvIngredient.setText(ingredient.getName());
        tvQuanity.setText(ingredient.getQuantity());
        return convertView;
    }
}

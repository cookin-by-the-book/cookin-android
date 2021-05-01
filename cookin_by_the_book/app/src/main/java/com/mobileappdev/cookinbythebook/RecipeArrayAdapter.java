package com.mobileappdev.cookinbythebook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobileappdev.cookinbythebook.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {

    private static final String TAG = "RecipeArrayAdapter";

    private Context mContext;
    int mResource;

    public RecipeArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipe> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String owner = getItem(position).getOwner();
        String picture = "";
        String combinedName = owner + "'s " + name;
        Map<String, String> ingredients = new HashMap<String, String>();
        String notes = "";
        ArrayList<String> sharedWith = new ArrayList<String>();
        ArrayList<String> steps = new ArrayList<String>();
        Recipe recipe = new Recipe(name, owner, picture, ingredients, notes, sharedWith, steps);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // set the text of the thing
        TextView tvName = (TextView) convertView.findViewById(R.id.recipeName);
        tvName.setText(combinedName);

        // set the picture
        /*
        Drawable drawable = getResources().getDrawable(R.drawable.s_vit);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.5),
                                 (int)(drawable.getIntrinsicHeight()*0.5));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, scaleWidth, scaleHeight);
        Button btn = findViewbyId(R.id.yourbtnID);
        btn.setCompoundDrawables(sd.getDrawable(), null, null, null); //set drawableLeft for example
         */
        return  convertView;
    }
}

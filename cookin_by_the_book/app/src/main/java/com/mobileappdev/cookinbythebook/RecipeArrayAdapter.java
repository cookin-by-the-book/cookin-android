//package com.mobileappdev.cookinbythebook;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {
//
//    private static final String TAG = "RecipeArrayAdapter";
//
//    private Context mContext;
//    int mResource;
//
//    public RecipeArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipe> objects) {
//        super(context, resource, objects);
//        mContext = context;
//        mResource = resource;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        String name = getItem(position).getName();
//        String owner = getItem(position).getOwner();
//        String combinedName = owner + "'s " + name;
//        Recipe recipe = new Recipe(name, owner);
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        convertView = inflater.inflate(mResource, parent, false);
//
//        TextView tvName = (TextView) convertView.findViewById(R.id.receipeName);
//        tvName.setText(combinedName);
//        return  convertView;
//    }
//}

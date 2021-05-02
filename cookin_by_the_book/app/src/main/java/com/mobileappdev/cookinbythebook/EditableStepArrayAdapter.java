package com.mobileappdev.cookinbythebook;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.ViewGroupUtils;

import java.util.ArrayList;

public class EditableStepArrayAdapter extends ArrayAdapter<Step> {
    public static final String TAG = "EditableStepArrayAdapter";

    private Context mContext;
    int mResource;

    public EditableStepArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Step> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Step step = new Step("");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        return convertView;
    }
}
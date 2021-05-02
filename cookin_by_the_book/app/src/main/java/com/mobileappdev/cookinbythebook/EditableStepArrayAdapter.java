package com.mobileappdev.cookinbythebook;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

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
        String step = getItem(position).getStep();
        Step myNewStep = new Step(step);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvStep = (TextView) convertView.findViewById(R.id.inputtedStep);
        tvStep.setText(myNewStep.getStep());
        return convertView;
    }
}
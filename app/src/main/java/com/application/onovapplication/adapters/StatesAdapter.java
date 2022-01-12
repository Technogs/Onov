package com.application.onovapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.application.onovapplication.model.statesData;

import java.util.List;

public class StatesAdapter extends ArrayAdapter<String> {
    private static final int ITEM_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int textViewResourceId;
    String[] states;

    public StatesAdapter(Context context,
                         int textViewResourceId,
                         String[] objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
        this.states = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView;

        if (convertView == null) {
            textView = (TextView) LayoutInflater.from(getContext())
                    .inflate(textViewResourceId, parent, false);
        } else {
            textView = (TextView) convertView;
        }



        textView.setText(getItem(position));
        Log.e("tesxtt",textView.getText().toString());
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.height = ITEM_HEIGHT;

        if (position == 0) {
            layoutParams.height = 1;
        } else {
            layoutParams.height = ITEM_HEIGHT;
        }
        textView.setLayoutParams(layoutParams);
        return textView;

    }
}

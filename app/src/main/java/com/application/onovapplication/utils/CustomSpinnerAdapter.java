package com.application.onovapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private static final int ITEM_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private final int textViewResourceId;

    public CustomSpinnerAdapter(Context context,
                                int textViewResourceId,
                                String[] objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
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
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        if (position == 0) {
            layoutParams.height = 1;
        } else {
            layoutParams.height = ITEM_HEIGHT;
        }
        textView.setLayoutParams(layoutParams);
        return textView;

    }
}
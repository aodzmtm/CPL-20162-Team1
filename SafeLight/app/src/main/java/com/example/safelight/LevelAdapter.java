package com.example.safelight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adnim on 2015-06-25.
 */
public class LevelAdapter extends ArrayAdapter<String> {
    private LayoutInflater mInflater;
    public LevelAdapter(Context context, ArrayList<String> object) {
        super(context, 0, object);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = null;

        if (v == null) {
            view = mInflater.inflate(R.layout.level_layout, null);
        } else {
            view = v;
        }
        final String data = this.getItem(position);
        if (data != null) {
            TextView tv = (TextView) view.findViewById(R.id.textView1);
            tv.setText(data);
        }
        return view;
    }
}
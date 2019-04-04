package com.example.weatherforecast.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.weatherforecast.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> dayArray;
    private final ArrayList<String> tempArray;

    public ListAdapter(Activity context, ArrayList<String> dayArray, ArrayList<String> tempArray) {
        super(context, R.layout.single_list_item, dayArray);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.dayArray = dayArray;
        this.tempArray = tempArray;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_list_item, null,true);

        TextView tv_day = rowView.findViewById(R.id.tv_day);
        TextView tv_day_temp = rowView.findViewById(R.id.tv_day_temp);

        tv_day.setText(dayArray.get(position));
        tv_day_temp.setText(tempArray.get(position));

        return rowView;

    };

}

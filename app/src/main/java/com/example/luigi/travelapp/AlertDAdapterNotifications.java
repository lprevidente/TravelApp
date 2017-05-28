package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.luigi.travelapp.costanti.Constants.NUM_TIMES;
import static com.example.luigi.travelapp.costanti.Constants.timeNotifications;

/**
 * Created by Luigi on 28/05/2017.
 */

public class AlertDAdapterNotifications extends BaseAdapter {
    private List<String> time = new ArrayList<>();
    private Context context;

    public AlertDAdapterNotifications(Context context) {
       for(int i=0; i<NUM_TIMES; i++){
            time.add(timeNotifications[i]);
        }
        this.context = context;
    }


    @Override
    public int getCount() {
        return time.size();
    }

    @Override
    public String getItem(int position) {
        return time.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_types_event, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.textViewDescrTyoeEvent);

        final String tmp = time.get(position);
        textView.setText(tmp);

        return view;
    }
}

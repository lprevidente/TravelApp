package com.example.luigi.travelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Day;

import java.util.Collections;
import java.util.List;

import static com.example.luigi.travelapp.TripListActivity.getDataStore;

/**
 * Created by Luigi on 08/05/2017.
 */

public class DayListAdapter extends BaseAdapter {
    private Context context;
    private List<Day> days = Collections.emptyList();
    private Intent intent;
    private int tripIndex;
    public static String DAY_INDEX = "";
    public static String TRIP_INDEX = "";

    public DayListAdapter(int tripIndex, Context context){
        this.context = context;
        this.tripIndex=tripIndex;
        days = getDataStore().getDayList(tripIndex);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.day_list_adapter, parent, false);

        TextView numDaytxt = (TextView)view.findViewById(R.id.dayTextView);

        // I obtain the Day object from the dataStore and put the text into the TextView
        numDaytxt.setText("Giorno " + days.get(position).getDayNumber());

        numDaytxt.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(context, EventListActivity.class);
                intent.putExtra(TRIP_INDEX, tripIndex);
                intent.putExtra(DAY_INDEX, position);
                context.startActivity(intent);
            }
        }));
        return view;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Day getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

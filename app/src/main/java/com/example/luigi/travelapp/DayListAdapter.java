package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigi.travelapp.datamodel.Day;

import java.util.Collections;
import java.util.List;

/**
 * Created by Luigi on 08/05/2017.
 */

public class DayListAdapter extends BaseAdapter {

    private Context context;
    private List<Day> days = Collections.emptyList();
    private Day day;

    public static final String TAG = "DayListAdapater";

    public DayListAdapter(int tripIndex, Context context){
        this.context=context;
        days=TripListActivity.dataStore.getDayList(tripIndex);
    }

    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.day_list_adapter, parent, false);

        TextView numDaytxt = (TextView)view.findViewById(R.id.dayTextView);

        // I obtain the Day object from the dataStore and put the text into the TextView
        day =  days.get(position);
        numDaytxt.setText("Giorno "+day.getDayNumber());

        numDaytxt.setOnClickListener((new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(context, "You Clicked the Day " +day.getDayNumber(), Toast.LENGTH_SHORT).show();
            }
        })) ;
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

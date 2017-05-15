package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Day;

import java.util.Collections;
import java.util.List;


/**
 * Created by Luigi on 08/05/2017.
 */

public class DayListAdapter extends BaseAdapter {
    private DataStore dataStore= DataStore.getInstance();

    private Context context;
    private List<Day> days = Collections.emptyList();
    private int tripIndex;


    public DayListAdapter(int tripIndex, Context context) {
        this.context = context;
        days = dataStore.getDayList(tripIndex);
        this.tripIndex = tripIndex;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.day_list_adapter, parent, false);

        TextView numDaytxt = (TextView)view.findViewById(R.id.dayTextView);

        TextView Event1txt = (TextView)view.findViewById(R.id.textViewEvent1);
        TextView Event2txt = (TextView)view.findViewById(R.id.textViewEvent2);
        TextView Event3txt = (TextView)view.findViewById(R.id.textViewEvent3);

        numDaytxt.setText("Giorno " + days.get(position).getDayNumber());
        if(!dataStore.getEventList(tripIndex, position).isEmpty()) {
            int sizeEventList =dataStore.getEventList(tripIndex, position).size();
                if (1<=sizeEventList)
                    Event1txt.setText(dataStore.getEventList(tripIndex, position).get(0).getTitle());
                if (2<=sizeEventList)
                    Event2txt.setText(dataStore.getEventList(tripIndex, position).get(1).getTitle());
                if (3<=sizeEventList)
                    Event3txt.setText(dataStore.getEventList(tripIndex, position).get(2).getTitle());

        }
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

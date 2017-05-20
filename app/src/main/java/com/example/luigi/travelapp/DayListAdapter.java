package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Day;
import com.example.luigi.travelapp.datamodel.Event;

import java.util.Collections;
import java.util.List;


/**
 * Created by Luigi on 08/05/2017.
 */

public class DayListAdapter extends BaseAdapter {

    private Context context;
    private List<Day> days = Collections.emptyList();
    DataStore dataStore = DataStore.getInstance();
    private List<Event> events = Collections.emptyList();

    public DayListAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Day> newList) {
        days = newList;
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.day_list_adapter, parent, false);

        TextView numDaytxt = (TextView)view.findViewById(R.id.dayTextView);

        TextView Event1txt = (TextView)view.findViewById(R.id.textViewEvent1);
        TextView Event2txt = (TextView)view.findViewById(R.id.textViewEvent2);
        TextView Event3txt = (TextView)view.findViewById(R.id.textViewEvent3);

        numDaytxt.setText(days.get(position).getNumber()+"°");


        // Mostro soltanto i primi 3 eventi di quel determinato giorno
        // todo: La lista degli eventi non è collegata con un determinato giorno
        // todo: Quando entro nell'activity non si vedono gli eventi a meno che non faccio un refresh
/*
        if(!dataStore.getEvents().isEmpty()) {
            int sizeEventList = events.size();
                if (1<=sizeEventList)
                    Event1txt.setText(events.get(0).getTitle());
            Log.i("Day_list_adapter", "Valore del dayKey" +events.get(1).getKey());
                if (2<=sizeEventList)
                    Event2txt.setText(events.get(1).getTitle());
                if (3<=sizeEventList)
                    Event3txt.setText(events.get(2).getTitle());
        }*/
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

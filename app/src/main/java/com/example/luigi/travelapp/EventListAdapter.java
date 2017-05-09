package com.example.luigi.travelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Event;

import java.util.Collections;
import java.util.List;

/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> events = Collections.emptyList();
    private Intent intent;
    public static final String EVENT_INDEX = "eventindex";

    //definisco il costruttore
    public EventListAdapter(Context context ) {
        this.context=context;
    }

    public void update(List<Event> newList) { events = newList; }

    // metodo per il ritorno della view
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.event_list_adapter, parent, false);

        //Ottengo gli ID
        TextView txtTitle = (TextView)view.findViewById(R.id.eventTextView);

        // Imposto i valori da visualizzare
        final Event event = events.get(position);
        txtTitle.setText(event.getTitle());

        /*txtTitle.setOnClickListener((new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(context, EventListActivity.class);
                intent.putExtra(EVENT_INDEX, Integer.toString(position));
                context.startActivity(intent);
            }
        }));*/
        return view;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

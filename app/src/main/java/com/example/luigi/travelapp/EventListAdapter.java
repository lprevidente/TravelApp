package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Event;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;



/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private List<Event> events = Collections.emptyList();

    public EventListAdapter(Context context ) {
        this.context = context;
    }

    public void update(List<Event> newList) { events = newList; }

    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.event_list_adapter, parent, false);

        final Event event = events.get(position);
        TextView txtTitle = (TextView)view.findViewById(R.id.eventTextView);
        txtTitle.setText(event.getTitle());

        ImageView imageView=(ImageView) view.findViewById(R.id.imageEvent);
        imageView.setImageResource(event.getImage());

        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        txtTime.setText(event.getTimeString());

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

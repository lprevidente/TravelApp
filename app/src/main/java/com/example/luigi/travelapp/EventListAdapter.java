package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Event;

import java.util.Collections;
import java.util.List;

import static com.example.luigi.travelapp.costanti.Constants.EVENT_TYPES_NUMBER;
import static com.example.luigi.travelapp.costanti.Constants.integers;
import static com.example.luigi.travelapp.costanti.Constants.textTypes;


/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private List<Event> events = Collections.emptyList();

    public EventListAdapter(Context context ) {
        this.context = context;
    }

    public void update(List<Event> newList) {
        events = newList;
        notifyDataSetChanged();
    }

    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.event_list_adapter, parent, false);

        final Event event = events.get(position);
        TextView txtTitle = (TextView)view.findViewById(R.id.eventTextView);
        txtTitle.setText(event.getTitle());

        ImageView imageView = (ImageView) view.findViewById(R.id.imageEvent);
        int index = getResourceIndex(event.getType());
        if (index != -1)
            imageView.setImageResource(integers[index]);

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

    private int getResourceIndex(String type) {
        int i = 0;
        while (i < EVENT_TYPES_NUMBER) {
            if (type.equals(textTypes[i]))
                return i;
            i++;
        }
        return -1;
    };
}

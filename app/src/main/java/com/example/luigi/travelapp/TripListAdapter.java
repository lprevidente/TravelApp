package com.example.luigi.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Trip;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Luigi on 08/05/2017.
 */

public class TripListAdapter extends BaseAdapter {

    private List<Trip> trips = Collections.emptyList();
    private Context context;

    public TripListAdapter(Context context) {
        this.context = context;
    }

    public void update(List<Trip> newList) {
        trips = newList;
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.trip_list_adapter, parent, false);

        TextView txtTitle = (TextView)view.findViewById(R.id.Text);
        TextView textViewDate = (TextView) view.findViewById(R.id.textViewDateTrip);
        TextView tripNotes = (TextView)view.findViewById(R.id.tripNotesText);
        final Trip trip = trips.get(position);
        txtTitle.setText(trip.getTitle());

        Calendar tmp = Calendar.getInstance();
        Calendar tmp2 = Calendar.getInstance();
        tmp.setTime(new Date(trip.getStartTime()));
        tmp2.setTime(new Date(trip.getEndTime()));
        setDate2TextView(tmp, tmp2,textViewDate);
        if (trip.getNotes().isEmpty())
            tripNotes.setText("Nessuna nota");
        else
            tripNotes.setText(trip.getNotes());

        return view;
    }

    private void setDate2TextView(Calendar calendar1, Calendar calendar2, TextView textView) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        textView.setText("Dal "+dateFormat.format(calendar1.getTime())+ " al "+ dateFormat.format(calendar2.getTime()));
    }

    @Override
    public int getCount() {
        return trips.size();
    }

    @Override
    public Trip getItem(int position) {
        return trips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

package com.example.luigi.travelapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.Trip;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Luigi on 08/05/2017.
 */

public class TripListAdapter extends BaseAdapter {

    private List<Trip> trips = Collections.emptyList();
    private Context context;

    public TripListAdapter(Context context ) {
        this.context = context;
    }

    public void update(List<Trip> newList) { trips = newList; }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.trip_list_adapter, parent, false);

        TextView txtTitle = (TextView)view.findViewById(R.id.Text);
        TextView textViewDate = (TextView) view.findViewById(R.id.textViewDateTrip);

        txtTitle.setTextColor(getRandomColor());

        final Trip trip = trips.get(position);
        txtTitle.setText(trip.getTitle());

        Calendar tmp = Calendar.getInstance();
        Calendar tmp2 = Calendar.getInstance();
        tmp.setTime(trip.getStartDate());
        tmp2.setTime(trip.getEndDate());
        setDate2TextView(tmp, tmp2,textViewDate);

        return view;
    }

    private void setDate2TextView(Calendar calendar1,Calendar calendar2, TextView textView) {
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

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}

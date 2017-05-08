package com.example.luigi.travelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigi.travelapp.datamodel.Trip;

import java.util.Collections;
import java.util.List;

/**
 * Created by Luigi on 08/05/2017.
 */

public class TripListAdapter extends BaseAdapter {

    private Context context;
    private List<Trip> trips = Collections.emptyList();
    private PopupMenu popupMenu ;
    private Intent intent;

    //definisco il costruttore
    public TripListAdapter(Context context ) {
        this.context=context;
    }

    public void update(List<Trip> newList) { trips = newList; }

    // metodo per il ritorno della view
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.trip_list_adapter, parent, false);

        //Ottengo gli ID
        TextView txtTitle = (TextView)view.findViewById(R.id.Text);

        // Imposto i valori da visualizzare
        final Trip trip =  trips.get(position);
        txtTitle.setText(trip.getTitleTrip());

        txtTitle.setOnClickListener((new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(context, "You Clicked at " +trip.getTitleTrip(), Toast.LENGTH_SHORT).show();

            }
        }));
        return view;
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

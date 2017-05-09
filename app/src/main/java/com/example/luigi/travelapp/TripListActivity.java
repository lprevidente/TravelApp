package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Trip;

public class TripListActivity extends AppCompatActivity {
    private static DataStore dataStore = new DataStore();

    private ListView list;
    private FloatingActionButton addcity;
    private TripListAdapter adapter;

    private final int code = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        adapter = new TripListAdapter(this);
        adapter.update(dataStore.getListTrip());

        addcity = (FloatingActionButton)findViewById(R.id.AddCity);

        list = (ListView)findViewById(R.id.dayListView);
        list.setAdapter(adapter);

        addcity.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripListActivity.this, CityActivity.class);
                startActivityForResult(intent, code);

            }
        }));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code) {
            if (resultCode == Activity.RESULT_OK) {
                Trip trip = (Trip) data.getSerializableExtra(CityActivity.mTrip);
                dataStore.addTrip(trip);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public static DataStore getDataStore() {
        return dataStore;
    }
}

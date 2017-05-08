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
    // context to be used by this activity. this is vital for the serialization functions and the creation of our DataStore object
    private static Context mContext;

    // our DataStore object. This is static to be persistent when reloading the activity
    public static DataStore dataStore;

    private ListView list;
    private FloatingActionButton addcity;
    private static TripListAdapter adapter;

    private final int code = 1;

    // local vars
    private String nameCity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        // get the application context and then initialize our dataStore
        mContext = getApplicationContext();
        dataStore = new DataStore(getContext());

        adapter = new TripListAdapter(this);
        adapter.update(dataStore.getListTrip());

        addcity = (FloatingActionButton)findViewById(R.id.AddCity);

        list = (ListView)findViewById(R.id.dayListView);
        list.setAdapter(adapter);

        // metodo per verificare il click del bottone
        addcity.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // i need to initialize the var modTrip so when I add a new travel,after i modified some items,
                // the autoCompleteTextView is empty
                // CityActivity.setTitletrip("");
                Intent intent = new Intent(TripListActivity.this, CityActivity.class);
                startActivityForResult(intent,code);

            }
        }));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code) {
            if (resultCode == Activity.RESULT_OK) {
                Trip trip = (Trip) data.getSerializableExtra(CityActivity.mTrip);
                // add the new trip in the data store
                dataStore.addTrip(trip);
                adapter.notifyDataSetChanged();
            }

        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static TripListAdapter getAdapter(){return adapter;}
}

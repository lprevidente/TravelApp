package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Trip;

import static com.example.luigi.travelapp.costanti.Constants.SEND_TRIP;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;

public class TripListActivity extends AppCompatActivity {
    private DataStore dataStore;
    private ListView list;
    private FloatingActionButton addcity;
    private TripListAdapter adapter;

    private final int code = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        dataStore = DataStore.getInstance(this.getApplicationContext());

        adapter = new TripListAdapter(this);
        adapter.update(dataStore.getListTrip());

        addcity = (FloatingActionButton)findViewById(R.id.AddCity);

        list = (ListView)findViewById(R.id.dayListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DayListActivity.class);
                intent.putExtra(TRIP_INDEX, position);
                startActivity(intent);
            }
        });

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
                Trip trip = (Trip) data.getSerializableExtra(SEND_TRIP);
                dataStore.addTrip(trip);
                adapter.notifyDataSetChanged();
            }
        }
    }
}

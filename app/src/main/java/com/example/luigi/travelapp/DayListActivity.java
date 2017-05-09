package com.example.luigi.travelapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class DayListActivity extends AppCompatActivity {

    private DayListAdapter dayListAdapter;
    private ListView listView;
    private int tripIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);

        Bundle extras = getIntent().getExtras();
        tripIndex = Integer.parseInt(extras.getString(TripListAdapter.TRIP_INDEX));

        dayListAdapter = new DayListAdapter(tripIndex, this);

        listView = (ListView)findViewById(R.id.dayListView);
        listView.setAdapter(dayListAdapter);
    }
}

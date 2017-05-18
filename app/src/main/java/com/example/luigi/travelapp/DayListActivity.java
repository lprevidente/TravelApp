package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;

import static com.example.luigi.travelapp.costanti.Constants.DAY_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;

public class DayListActivity extends Activity {

    private DataStore dataStore = DataStore.getInstance();
    private DayListAdapter dayListAdapter;
    private ListView listView;
    private int tripIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);

        Bundle extras = getIntent().getExtras();
        tripIndex = extras.getInt(TRIP_INDEX);

        dayListAdapter = new DayListAdapter(tripIndex, this);
        dataStore.beginTripsObs(new DataStore.UpdateListener() {
            @Override
            public void tripsUpdated() {
                dayListAdapter.update();
            }
        });

        listView = (ListView)findViewById(R.id.dayListView);
        listView.setAdapter(dayListAdapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EventListActivity.class);
                intent.putExtra(TRIP_INDEX, tripIndex);
                intent.putExtra(DAY_INDEX, position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataStore.endTripsObs();
    }
}

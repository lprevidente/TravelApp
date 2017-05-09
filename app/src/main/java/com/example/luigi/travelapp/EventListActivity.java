package com.example.luigi.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import static com.example.luigi.travelapp.DayListAdapter.DAY_INDEX;
import static com.example.luigi.travelapp.TripListActivity.getDataStore;
import static com.example.luigi.travelapp.TripListAdapter.TRIP_INDEX;

/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListActivity extends AppCompatActivity{
    private ListView list;
    private FloatingActionButton addEvent;
    private EventListAdapter eventListAdapter;
    private int tripIndex;
    private int dayIndex;

    private final int code = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Bundle extras = getIntent().getExtras();
        tripIndex = extras.getInt(TRIP_INDEX);
        dayIndex = extras.getInt(DAY_INDEX);

        eventListAdapter = new EventListAdapter(this);
        eventListAdapter.update(getDataStore().getEventList(tripIndex, dayIndex));

        addEvent = (FloatingActionButton)findViewById(R.id.AddEvent);

        list = (ListView)findViewById(R.id.eventListView);
        list.setAdapter(eventListAdapter);

        // metodo per verificare il click del bottone
        addEvent.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, EventActivity.class);
                startActivityForResult(intent, code);
            }
        }));
    }
}

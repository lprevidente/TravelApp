package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.Event;

import static com.example.luigi.travelapp.TripListActivity.getDataStore;
import static com.example.luigi.travelapp.costanti.Constants.DAY_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;

/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListActivity extends Activity{

    private ListView list;
    private FloatingActionButton addEvent;
    private EventListAdapter eventListAdapter;
    private int tripIndex;
    private int dayIndex;

    private final int CODE = 2;

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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EventActivity.class);
                //intent.putExtra( , );
                startActivity(intent);
            }
        });

        addEvent.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, EventActivity.class);
                startActivityForResult(intent, CODE);
            }
        }));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Event event = (Event) data.getSerializableExtra(EventActivity.EVENT);
                // add the new trip in the data store
                TripListActivity.dataStore.addEvent(tripIndex, dayIndex,event);
                TripListActivity.adapter.notifyDataSetChanged();
            }
        }
    }
}

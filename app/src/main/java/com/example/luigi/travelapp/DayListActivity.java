package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;

import static com.example.luigi.travelapp.costanti.Constants.DAY_REFERENCE;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_REFERENCE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;

public class DayListActivity extends Activity {

    private DataStore dataStore = DataStore.getInstance();
    private DayListAdapter dayListAdapter;
    private ListView listView;
    private String dayReference;
    private String tripKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);

        Bundle extras = getIntent().getExtras();
        tripKey = extras.getString(KEY_TRIP);
        dayReference = extras.getString(DAY_REFERENCE);

        dayListAdapter = new DayListAdapter(this);
        dataStore.beginDaysObs(new DataStore.UpdateListener() {
            @Override
            public void tripsUpdated() {}

            @Override
            public void daysUpdated() {
                dayListAdapter.update(dataStore.getDays());
            }

            @Override
            public void eventsUpdated() {}
        }, dayReference);


        listView = (ListView)findViewById(R.id.dayListView);
        listView.setAdapter(dayListAdapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EventListActivity.class);
                intent.putExtra(KEY_TRIP, tripKey);
                intent.putExtra(KEY_DAY, dataStore.getDays().get(position).getKey());
                intent.putExtra(EVENT_REFERENCE, dataStore.getDays().get(position).getEventsReference());
                startActivity(intent);
            }
        });
    }

    /**
     *  when the user add a new event and go back to this activity
     *  there is a refresh of the listView
     */
    protected void onResume(){
        super.onResume();
        dayListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataStore.endDaysObs();
    }
}

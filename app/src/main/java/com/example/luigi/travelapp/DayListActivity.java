package com.example.luigi.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.example.luigi.travelapp.costanti.Constants.DAY_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;

public class DayListActivity extends AppCompatActivity {

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

        listView = (ListView)findViewById(R.id.dayListView);
        listView.setAdapter(dayListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EventListActivity.class);
                intent.putExtra(TRIP_INDEX, tripIndex);
                intent.putExtra(DAY_INDEX, position);
                startActivity(intent);
            }
        });
    }
}

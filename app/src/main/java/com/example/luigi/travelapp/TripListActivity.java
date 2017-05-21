package com.example.luigi.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.luigi.travelapp.costanti.Constants.FIRSTLAUNCH;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;
import static com.example.luigi.travelapp.costanti.Constants.NULLTITLE;

public class TripListActivity extends AppCompatActivity {

    private DataStore dataStore;

    private ListView list;
    private FloatingActionButton addcity;
    private TripListAdapter adapter;
    private Toolbar toolbar;
    private Menu menu;

    private int posizione;

    FirebaseAuth mAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        dataStore = DataStore.getInstance();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        list = (ListView)findViewById(R.id.dayListView);
        adapter = new TripListAdapter(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(TripListActivity.this, LoginActivity.class);
            intent.putExtra(FIRSTLAUNCH, true);
            startActivity(intent);
        }
        else {
            dataStore.beginTripsObs(new DataStore.UpdateListener() {
                @Override
                public void tripsUpdated() {
                    adapter.update(dataStore.getTrips());
                }

                @Override
                public void eventsUpdated() { }

                @Override
                public void daysUpdated() { }
            });
        }

        list.setAdapter(adapter);

        toolbar = (Toolbar)findViewById(R.id.toolbar_trip_list);
        toolbar.setTitle(R.string.titleCities);
        toolbar.inflateMenu(R.menu.menu_list_events);
        menu = toolbar.getMenu();

        addcity = (FloatingActionButton)findViewById(R.id.AddCity);
        addcity.setImageResource(R.drawable.ic_action_name_add);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                EditToolbar();
                posizione = position;
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DefaulToolbar();
                Intent intent = new Intent(view.getContext(), DayListActivity.class);
                intent.putExtra(KEY_TRIP, dataStore.getTrips().get(position).getKey());
                startActivity(intent);
            }
        });

        addcity.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar();
                Intent intent = new Intent(TripListActivity.this, CityActivity.class);
                intent.putExtra(KEY_TRIP, -1);
                startActivityForResult(intent, 0);
            }
        }));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        dataStore.deleteTrip(dataStore.getTrips().get(posizione).getKey());
                        DefaulToolbar();
                        return true;
                    case R.id.item_edit:
                        DefaulToolbar();
                        Intent intent = new Intent(TripListActivity.this, CityActivity.class);
                        intent.putExtra(KEY_TRIP, posizione);
                        startActivityForResult(intent, 0);
                }
                return false;
            }
        });
    }

    private void DefaulToolbar(){
        // toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        toolbar.setTitle(R.string.titleCities);
        toolbar.setNavigationIcon(null);
        menu.findItem(R.id.item_edit).setVisible(false);
        menu.findItem(R.id.item_delete).setVisible(false);
    }

    private void EditToolbar(){
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitle(NULLTITLE);
        toolbar.setNavigationIcon(R.drawable.ic_action_name_back);
        menu.findItem(R.id.item_edit).setVisible(true);
        menu.findItem(R.id.item_delete).setVisible(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataStore.endTripsObs();
    }
}

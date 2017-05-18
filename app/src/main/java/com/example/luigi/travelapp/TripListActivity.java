package com.example.luigi.travelapp;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.luigi.travelapp.costanti.Constants.EVENT;
import static com.example.luigi.travelapp.costanti.Constants.EVENTNEW;
import static com.example.luigi.travelapp.costanti.Constants.SEND_TRIP;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;

public class TripListActivity extends AppCompatActivity {
    private DataStore dataStore;
    private ListView list;
    private FloatingActionButton addcity;
    private TripListAdapter adapter;
    private Toolbar toolbar;
    private Menu menu;
    private int posizione;

    private Intent intent;

    private final int CODE = 1;
    private final int CODE4=4;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // devo fare il login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            // sono già loggato
            Toast.makeText(getApplicationContext(), "Loggato come: "+  user.getEmail(), Toast.LENGTH_SHORT).show();
        }

        dataStore = DataStore.getInstance();

        adapter = new TripListAdapter(this);
        adapter.update(dataStore.getListTrip());

        toolbar = (Toolbar)findViewById(R.id.toolbar_trip_list);
        toolbar.setTitle(R.string.titleCities);
        toolbar.inflateMenu(R.menu.menu_list_events);
        menu = toolbar.getMenu();

        addcity = (FloatingActionButton)findViewById(R.id.AddCity);

        list = (ListView)findViewById(R.id.dayListView);
        list.setAdapter(adapter);


        addcity.setImageResource(R.drawable.ic_action_name_add);

        /**
         * Make the two icon, DELETE and EDIT, visible on a long Click
         *
         */

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
                intent.putExtra(TRIP_INDEX, position);
                startActivity(intent);
            }
        });

        addcity.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar();
                intent = new Intent(TripListActivity.this, CityActivity.class);
                intent.putExtra(EVENTNEW,"yes");
                startActivityForResult(intent, CODE);
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
                        dataStore.deleteTrip(posizione);
                        adapter.notifyDataSetChanged();
                        DefaulToolbar();
                        return true;
                    case R.id.item_edit:
                        DefaulToolbar();
                        intent= new Intent(TripListActivity.this, CityActivity.class);
                        intent.putExtra(EVENT, dataStore.getListTrip().get(posizione));
                        intent.putExtra(EVENTNEW, "NO");
                        startActivityForResult(intent, CODE4);
                }
                return false;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Trip trip = (Trip) data.getSerializableExtra(SEND_TRIP);
                dataStore.addTrip(trip);
                adapter.notifyDataSetChanged();
                menu.findItem(R.id.item_edit).setVisible(false);
                menu.findItem(R.id.item_delete).setVisible(false);
            }
        }
        if(requestCode==CODE4){
            if(resultCode==Activity.RESULT_OK){
                Trip trip = (Trip) data.getSerializableExtra(EVENT);
                dataStore.updateTrip(posizione, trip);
                adapter.notifyDataSetChanged();
                menu.findItem(R.id.item_edit).setVisible(false);
                menu.findItem(R.id.item_delete).setVisible(false);
            }
        }
    }

    private void DefaulToolbar(){
        //toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        toolbar.setTitle(R.string.titleCities);
        toolbar.setNavigationIcon(null);
        menu.findItem(R.id.item_edit).setVisible(false);
        menu.findItem(R.id.item_delete).setVisible(false);

    }

    private void EditToolbar(){
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_action_name_back);
        menu.findItem(R.id.item_edit).setVisible(true);
        menu.findItem(R.id.item_delete).setVisible(true);

    }
}

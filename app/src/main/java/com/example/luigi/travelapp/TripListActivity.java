package com.example.luigi.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
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

    private View mview=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        dataStore = DataStore.getInstance();

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
            dataStore.attachTripsListener(new DataStore.UpdateListener() {
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
        toolbar.inflateMenu(R.menu.menu_trip_list);
        menu = toolbar.getMenu();

        addcity = (FloatingActionButton)findViewById(R.id.AddCity);
        addcity.setImageResource(R.drawable.ic_action_name_add);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(mview!=null)
                    mview.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.colorTransparet, null));
                mview=view;
                EditToolbar(mview);
                posizione = position;
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mview=view;
                DefaulToolbar(mview);
                Intent intent = new Intent(view.getContext(), DayListActivity.class);
                intent.putExtra(KEY_TRIP, dataStore.getTrips().get(position).getKey());
                startActivity(intent);
            }
        });

        addcity.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar(mview);
                Intent intent = new Intent(TripListActivity.this, CityActivity.class);
                intent.putExtra(KEY_TRIP, -1);
                startActivityForResult(intent, 0);
            }
        }));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar(mview);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        dataStore.deleteTrip(dataStore.getTrips().get(posizione).getKey());
                        DefaulToolbar(mview);
                        return true;
                    case R.id.item_edit:
                        DefaulToolbar(mview);
                        Intent intent = new Intent(TripListActivity.this, CityActivity.class);
                        intent.putExtra(KEY_TRIP, posizione);
                        startActivity(intent);
                        return true;
                    case  R.id.item_logout:
                        mAuth.signOut();
                        Intent intent1 = new Intent(TripListActivity.this, LoginActivity.class);
                        intent1.putExtra(FIRSTLAUNCH, true);
                        startActivity(intent1);
                        return true;
                    case R.id.item_info:
                        Intent intent2 = new Intent(TripListActivity.this, CreditsActivity.class);
                        startActivity(intent2);
                }
                return false;
            }
        });
    }

    private void DefaulToolbar(View view){
        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        toolbar.setTitle(R.string.titleCities);
        toolbar.setNavigationIcon(null);

        menu.findItem(R.id.item_info).setVisible(true);
        menu.findItem(R.id.item_logout).setVisible(true);

        menu.findItem(R.id.item_edit).setVisible(false);
        menu.findItem(R.id.item_delete).setVisible(false);
        if(view!= null)
            view.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.colorTransparet, null));
    }

    private void EditToolbar(View view){
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitle(NULLTITLE);
        toolbar.setNavigationIcon(R.drawable.ic_action_name_back);

        menu.findItem(R.id.item_info).setVisible(false);
        menu.findItem(R.id.item_logout).setVisible(false);

        menu.findItem(R.id.item_edit).setVisible(true);
        menu.findItem(R.id.item_delete).setVisible(true);

        if(view!= null)
            view.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.colorHilightGrey, null));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataStore.removeTripsListener();
    }
}

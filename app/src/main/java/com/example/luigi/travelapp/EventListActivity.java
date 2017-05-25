package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;

import static com.example.luigi.travelapp.costanti.Constants.EVENT;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP;

/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListActivity extends Activity{
    private DataStore dataStore = DataStore.getInstance();

    private ListView list;
    private FloatingActionButton addEvent;
    private EventListAdapter eventListAdapter;
    private Toolbar toolbar;
    private Menu menu;
    private int posizione;

    String eventReference;
    String tripKey;
    String dayKey;


    private View mview=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Bundle extras = getIntent().getExtras();
        tripKey = extras.getString(KEY_TRIP);
        dayKey = extras.getString(KEY_DAY);
        eventReference = extras.getString(KEY_DAY);

        eventListAdapter = new EventListAdapter(this);

        dataStore.beginEventsObs(new DataStore.UpdateListener() {
            @Override
            public void tripsUpdated() {}

            @Override
            public void daysUpdated() {}

            @Override
            public void eventsUpdated() {
                eventListAdapter.update(dataStore.getEvents());
            }
        }, eventReference);

        addEvent = (FloatingActionButton)findViewById(R.id.AddEvent);

        toolbar = (Toolbar)findViewById(R.id.toolbar_event_list);
        toolbar.inflateMenu(R.menu.menu_list_events);
        menu = toolbar.getMenu();

        list = (ListView)findViewById(R.id.eventListView);
        list.setAdapter(eventListAdapter);

        addEvent.setImageResource(R.drawable.ic_action_name_add);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                posizione = position;
                if(mview!=null)
                    mview.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.colorTransparet, null));
                mview = view;
                EditToolbar(mview);
                posizione = position;

                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DefaulToolbar(mview);
                Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
                intent.putExtra(EVENT, position);
                startActivity(intent);
            }
        });

        /**
         * Mi serve per gesitre gli hendler dei due Item posti nella Toolbar
         * Delete: cancello il dato dal datastore
         * Edit: Ho la possibilità di modificare il mio evento
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        dataStore.deleteEvent(eventReference, dataStore.getEvents().get(posizione).getKey());
                        eventListAdapter.notifyDataSetChanged();
                        DefaulToolbar(mview);
                        return true;

                    case R.id.item_edit:
                        DefaulToolbar(mview);
                        Intent intent = new Intent(EventListActivity.this, EventActivity.class);
                        intent.putExtra(KEY_EVENT, posizione);
                        intent.putExtra(KEY_TRIP, tripKey);
                        intent.putExtra(KEY_DAY, dayKey);
                        startActivityForResult(intent, 0);
                }
                return false;
            }
        });

        addEvent.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar(mview);
                Intent intent = new Intent(EventListActivity.this, EventActivity.class);
                intent.putExtra(KEY_EVENT, -1);
                intent.putExtra(KEY_TRIP, tripKey);
                intent.putExtra(KEY_DAY, dayKey);
                startActivity(intent);
            }
        }));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DefaulToolbar(mview);
            }
        });
    }


    private void DefaulToolbar(View view){
        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        toolbar.setTitle(R.string.titleEvents);
        toolbar.setNavigationIcon(null);
        menu.findItem(R.id.item_edit).setVisible(false);
        menu.findItem(R.id.item_delete).setVisible(false);
        if(view!= null)
        view.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.colorTransparet, null));

    }

    private void EditToolbar(View view){
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_action_name_back);
        menu.findItem(R.id.item_edit).setVisible(true);
        menu.findItem(R.id.item_delete).setVisible(true);
        if(view!= null)
        view.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.colorHilightGrey, null));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataStore.endEventsObs();
    }
}

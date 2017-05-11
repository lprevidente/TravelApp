package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Event;

import static com.example.luigi.travelapp.costanti.Constants.DAY_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.TRIP_INDEX;

/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListActivity extends Activity{
    private DataStore dataStore= DataStore.getInstance();

    private ListView list;
    private FloatingActionButton addEvent;
    private EventListAdapter eventListAdapter;
    private int tripIndex;
    private int dayIndex;
    private Toolbar toolbar;
private Menu menu;
    private int positione;
    private final int CODE = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Bundle extras = getIntent().getExtras();
        tripIndex = extras.getInt(TRIP_INDEX);
        dayIndex = extras.getInt(DAY_INDEX);

        eventListAdapter = new EventListAdapter(this);
        eventListAdapter.update(dataStore.getEventList(tripIndex, dayIndex));

        addEvent = (FloatingActionButton)findViewById(R.id.AddEvent);

        toolbar= (Toolbar) findViewById(R.id.toolbar_event_list);
        toolbar.inflateMenu(R.menu.menu_list_events);
        menu= toolbar.getMenu();

        list = (ListView)findViewById(R.id.eventListView);
        list.setAdapter(eventListAdapter);


        /**
         * se c'è un click lungo fa compare sulla toolbar due icone una serve per modificare e l'altra
         * per cancellare l'evento
         */
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                menu.findItem(R.id.item_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                menu.findItem(R.id.item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                menu.findItem(R.id.item_edit).setVisible(true);
                menu.findItem(R.id.item_delete).setVisible(true);
                positione=position;

            return true;
            }

        });
    /**
     * Nel caso volessi tornare indietro dopo il tocco prolungato clicco semplicemente su uno dei tanti eventi presenti
     */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                menu.findItem(R.id.item_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                menu.findItem(R.id.item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                menu.findItem(R.id.item_edit).setVisible(false);
                menu.findItem(R.id.item_delete).setVisible(false);
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
                        dataStore.deleteEvent(tripIndex, dayIndex, positione );
                        eventListAdapter.notifyDataSetChanged();
                        menu.findItem(R.id.item_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                        menu.findItem(R.id.item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                        menu.findItem(R.id.item_edit).setVisible(false);
                        menu.findItem(R.id.item_delete).setVisible(false);
                        return true;

                }
                return false;
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
                Event event = (Event) data.getSerializableExtra(EVENT_INDEX);
                // add the new trip in the data store
                dataStore.addEvent(tripIndex, dayIndex,event);
                eventListAdapter.notifyDataSetChanged();
            }
        }
    }
}

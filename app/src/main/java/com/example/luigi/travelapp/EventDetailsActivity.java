package com.example.luigi.travelapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Event;

import static com.example.luigi.travelapp.costanti.Constants.EVENT;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_TYPES_NUMBER;
import static com.example.luigi.travelapp.costanti.Constants.backgrounds;
import static com.example.luigi.travelapp.costanti.Constants.textTypes;

public class EventDetailsActivity extends Activity {

    TextView eventNotes;
    TextView eventTitle;
    TextView eventTime;
    ImageView eventBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        DataStore dataStore = DataStore.getInstance();
        Bundle extras = getIntent().getExtras();
        int pos = extras.getInt(EVENT);

        Event event = dataStore.getEvents().get(pos);
        int resNumber = getResourceIndex(event.getType().toString());

        eventBackgroundImageView = (ImageView)findViewById(R.id.eventBackgroundImageView);

        if (resNumber != -1) {
            eventBackgroundImageView.setImageResource(backgrounds[resNumber]);
        }

        eventTitle = (TextView)findViewById(R.id.eventTitle);
        eventTitle.setText(event.getTitle());

        eventTime = (TextView)findViewById(R.id.eventTime);
        eventTime.setText(event.getTimeString());

        eventNotes = (TextView)findViewById(R.id.eventNotes);
        eventNotes.setText(event.getNote());
    }

    private int getResourceIndex(String type) {
        int i = 0;
        while (i < EVENT_TYPES_NUMBER) {
            if (type.equals(textTypes[i]))
                return i;
            i++;
        }
        return -1;
    };
}

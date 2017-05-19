package com.example.luigi.travelapp.datamodel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_EVENT_LIST_REFERENCE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_DAY_NUMBER;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_IMAGE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_NOTE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_NOTIFY;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_EVENT_TITLE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_DAY_LIST_REFERENCE;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_END_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_LIST;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_START_TIME;
import static com.example.luigi.travelapp.costanti.Constants.KEY_TRIP_TITLE;

/**
 * Created by Bernardo on 08/05/2017.
 */

public class DataStore {
    private ArrayList<Trip> trips;
    private ArrayList<Day> days;
    private ArrayList<Event> events;

    private static DataStore dataStore = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ValueEventListener listenerTrips;
    private ValueEventListener listenerDays;
    private ValueEventListener listenerEvents;

    private DataStore() {
        trips = new ArrayList<>();
        days = new ArrayList<>();
        events = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (dataStore == null){
            dataStore = new DataStore();
        }
        return dataStore;
    }

    public interface UpdateListener {
        void tripsUpdated();
        void daysUpdated();
        void eventsUpdated();
    }

    public void beginTripsObs(final UpdateListener notification) {
        DatabaseReference reference = database.getReference(user.getUid()).child(KEY_TRIP_LIST);

        listenerTrips = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trips.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Trip trip = new Trip();

                    trip.setTitle(item.child(KEY_TRIP_TITLE).getValue(String.class));
                    trip.setStartTime(item.child(KEY_TRIP_START_TIME).getValue(Long.class));
                    trip.setEndTime(item.child(KEY_TRIP_END_TIME).getValue(Long.class));
                    trip.setDaysReference(item.child(KEY_TRIP_DAY_LIST_REFERENCE).getValue(String.class));

                    trips.add(trip);
                }
                notification.tripsUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerTrips);
    }

    public void beginDaysObs(final UpdateListener notification, String dayReference) {
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .child(dayReference);

        listenerDays = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                days.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Day day = new Day();

                    day.setNumber(item.child(KEY_DAY_NUMBER).getValue(Integer.class));
                    day.setEventsReference(item.child(KEY_DAY_EVENT_LIST_REFERENCE).getValue(String.class));

                    days.add(day);
                }
                notification.daysUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerDays);
    }

    public void beginEventsObs(final UpdateListener notification, String eventReference) {
        DatabaseReference reference = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .child(eventReference);

        listenerEvents = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Event event = new Event();

                    event.setTitle(item.child(KEY_EVENT_TITLE).getValue(String.class));
                    event.setNote(item.child(KEY_EVENT_NOTE).getValue(String.class));
                    event.setImage(item.child(KEY_EVENT_IMAGE).getValue(Integer.class));
                    event.setNotify(item.child(KEY_EVENT_NOTIFY).getValue(Boolean.class));
                    event.setTime(item.child(KEY_EVENT_TIME).getValue(Long.class));

                    events.add(event);
                }
                notification.eventsUpdated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerEvents);
    }

    public void endTripsObs() {
        if (listenerTrips != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_TRIP_LIST).removeEventListener(listenerTrips);
    }

    public void endDaysObs() {
        if (listenerDays != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_DAY_LIST).removeEventListener(listenerDays);
    }

    public void endEventsObs() {
        if (listenerEvents != null)
            FirebaseDatabase.getInstance().getReference(user.getUid()).child(KEY_EVENT_LIST).removeEventListener(listenerEvents);
    }

    public void addTrip(Trip trip) {
        int daysNumber = trip.getDaysNumber();

        // creo una nuova chiave per la lista dei giorni del viaggio
        String dayReference = database.getReference(user.getUid())
                .child(KEY_DAY_LIST)
                .push()
                .getKey();

        // setto la chiave nel viaggio
        trip.setDaysReference(dayReference);

        for (int i = 0; i < daysNumber; i++) {
            addDay(new Day(i + 1), dayReference);
        }

        DatabaseReference reference = database.getReference(user.getUid()).child(KEY_TRIP_LIST).push();
        reference.setValue(trip);
    }

    public void addDay(Day day, String dayReference) {
        // creo una nuova chiave per la lista degli eventi del giorno
        String eventReference = database.getReference(user.getUid())
                .child(KEY_EVENT_LIST)
                .push()
                .getKey();

        // setto la chiave nel giorno
        day.setEventsReference(eventReference);

        DatabaseReference reference = database.getReference(user.getUid()).child(KEY_DAY_LIST).child(dayReference).push();
        reference.setValue(day);
    }

    public void addEvent(Event event, String eventReference) {
        DatabaseReference reference = database.getReference(user.getUid()).child(KEY_EVENT_LIST).child(eventReference).push();
        reference.setValue(event);
    }

    public void updateTrip(int tripIndex, Trip trip) {

    }

    public void updateEvent(int tripIndex, int dayIndex, int eventIndex, Event event) {

    }

    public void deleteTrip(int i) {
        /*DatabaseReference reference = database.getReference(user.getUid()).push();
        reference.removeValue();*/
    }

    public void deleteEvent(int tripIndex, int dayIndex, int eventIndex) {
        /*DatabaseReference reference = database.getReference(user.getUid())
                .child(Integer.toString(tripIndex)).child(KEY_TRIP_DAY_LIST)
                .child(Integer.toString(dayIndex))
                .child(KEY_DAY_EVENT_LIST)
                .child(Integer.toString(eventIndex));
        reference.removeValue();*/
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
